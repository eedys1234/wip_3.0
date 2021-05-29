package com.wip.bool.music.song.service;

import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.bible.domain.WordsMasterRepository;
import com.wip.bool.cmmn.dictionary.SearchStoreProxy;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.type.SortType;
import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.guitar.domain.GuitarCodeRepository;
import com.wip.bool.music.mp3.domain.SongMP3;
import com.wip.bool.music.mp3.domain.SongMP3Repository;
import com.wip.bool.music.sheet.domain.SongSheet;
import com.wip.bool.music.sheet.domain.SongSheetRepository;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.music.song.domain.SongMasterRepository;
import com.wip.bool.music.song.dto.SongDetailDto;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SongDetailService {

    @Value("${spring.images.path}")
    private String imageFilePath;

    @Value("${spring.mp3.path}")
    private String mp3FilePath;

    private final SearchStoreProxy searchStore;

    private final SongDetailRepository songDetailRepository;

    private final SongMasterRepository songMasterRepository;

    private final GuitarCodeRepository guitarCodeRepository;

    private final WordsMasterRepository wordsMasterRepository;

    private final SongSheetRepository songSheetRepository;

    private final SongMP3Repository songMP3Repository;

    private final UserRepository userRepository;

    @Transactional
    public Long saveSong(Long userId, SongDetailDto.SongDetailSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role != Role.ROLE_ADMIN) {
            throw new AuthorizationException();
        }

        SongMaster songMaster = selectedSongMaster(requestDto.getCodeId());

        GuitarCode guitarCode = selectedGuitarCode(requestDto.getGuitarCodeId());

        WordsMaster wordsMaster = selectedWordsMaster(requestDto.getWordsMasterId());

        SongDetail songDetail = SongDetail.createSongDetail(requestDto.getTitle(), requestDto.getLyrics(), songMaster,
                guitarCode, wordsMaster);

        Long id = songDetailRepository.save(songDetail).getId();

        searchStore.insert(requestDto.getTitle());

        return id;
    }

    @Transactional
    public Long updateSong(Long userId, Long songDetailId, SongDetailDto.SongDetailUpdateRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role != Role.ROLE_ADMIN) {
            throw new AuthorizationException();
        }

        SongDetail songDetail = selectedSongDetail(songDetailId);

        Optional.ofNullable(requestDto.getCodeId())
                .ifPresent(codeId -> songDetail.updateSongMaster(selectedSongMaster(codeId)));

        Optional.ofNullable(requestDto.getGuitarCodeId())
                .ifPresent(guitarCodeId -> songDetail.updateGuitarCode(selectedGuitarCode(guitarCodeId)));

        Optional.ofNullable(requestDto.getWordsMasterId())
                .ifPresent(wordsMasterId -> songDetail.updateWordsMaster(selectedWordsMaster(wordsMasterId)));

        Optional.ofNullable(requestDto.getTitle()).ifPresent(title -> {
            searchStore.delete(songDetail.getTitle());
            songDetail.updateTitle(title);
            searchStore.insert(songDetail.getTitle());
        });

        Optional.ofNullable(requestDto.getLyrics()).ifPresent(songDetail::updateLyrics);

        return songDetail.getId();
    }

    @Transactional
    public Long deleteSong(Long userId, Long songDetailId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role != Role.ROLE_ADMIN) {
            throw new AuthorizationException();
        }

        boolean isDeleteSheet = true;
        boolean isDeleteMP3 = true;

        SongDetail songDetail = selectedSongDetail(songDetailId);

        songDetailRepository.delete(songDetail);

        List<SongSheet> songSheets = songSheetRepository.findBySongDetail(songDetail.getId());
        SongMP3 songMP3 = songMP3Repository.findBySongDetail(songDetailId)
                .orElseThrow(() -> new EntityNotFoundException(songDetailId, ErrorCode.NOT_FOUND_MP3));

        if(!songSheets.isEmpty() && songSheets.size() > 0) {
            isDeleteSheet = songSheets.stream().allMatch(sheet -> sheet.deleteSheetFile(imageFilePath));
            if(!isDeleteSheet) throw new IllegalStateException("악보파일 삭제가 실패했습니다.");
        }

        if(!Objects.isNull(songMP3)) {
            isDeleteMP3 = songMP3.deleteMP3File(mp3FilePath);
            if(!isDeleteMP3) throw new IllegalStateException("MP3파일 삭제가 실패했습니다.");
        }

        searchStore.delete(songDetail.getTitle());

        return 1L;
    }

    @Transactional(readOnly = true)
    public List<SongDetailDto.SongDetailSimpleResponse> findAll(Long songMasterId, String order, String sort,
                                                       int size, int offset) {

        SongMaster songMaster = null;
        if(songMasterId != 0 && !Objects.isNull(songMasterId)) {
            songMaster = selectedSongMaster(songMasterId);
        }

        SortType sortType = SortType.valueOf(sort.toUpperCase());
        OrderType orderType = OrderType.valueOf(order.toUpperCase());

        if(Objects.isNull(sortType) || Objects.isNull(orderType)) {
            throw new IllegalArgumentException();
        }

        return songDetailRepository.findAll(songMaster, sortType, orderType, size, offset);
    }

    @Transactional(readOnly = true)
    public SongDetailDto.SongDetailResponse findDetailOne(Long songDetailId, Long userId) {
        return songDetailRepository.findById(songDetailId, userId)
                .orElseThrow(() -> new EntityNotFoundException(songDetailId, ErrorCode.NOT_FOUND_SONG));
    }

    public List<SongDetailDto.SongDetailSimpleResponse> search(String keyword) {
        return searchStore.findWords(keyword)
                .stream()
                .map(SongDetailDto.SongDetailSimpleResponse::new)
                .collect(Collectors.toList());
    }

    private SongDetail selectedSongDetail(Long songDetailId) {
        return songDetailRepository.findById(songDetailId)
                .orElseThrow(() -> new EntityNotFoundException(songDetailId, ErrorCode.NOT_FOUND_SONG));
    }

    private WordsMaster selectedWordsMaster(Long wordsMasterId) {
        return wordsMasterRepository.findById(wordsMasterId)
                .orElseThrow(() -> new IllegalArgumentException("words master id가 존재하지 않습니다. "));
    }

    private GuitarCode selectedGuitarCode(Long guitarCodeId) {
        return guitarCodeRepository.findById(guitarCodeId)
                .orElseThrow(() -> new EntityNotFoundException(guitarCodeId, ErrorCode.NOT_FOUND_GUITAR_CODE));
    }

    private SongMaster selectedSongMaster(Long songMasterId) {
        return songMasterRepository.findById(songMasterId)
                .orElseThrow(() -> new EntityNotFoundException(songMasterId, ErrorCode.NOT_FOUND_SONG_MASTER));
    }
}
