package com.wip.bool.service.music.song;

import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.type.SortType;
import com.wip.bool.domain.bible.WordsMaster;
import com.wip.bool.domain.bible.WordsMasterRepository;
import com.wip.bool.cmmn.dictionary.SearchStore;
import com.wip.bool.domain.music.guitar.GuitarCode;
import com.wip.bool.domain.music.guitar.GuitarCodeRepository;
import com.wip.bool.domain.music.mp3.SongMP3;
import com.wip.bool.domain.music.mp3.SongMP3Repository;
import com.wip.bool.domain.music.sheet.SongSheet;
import com.wip.bool.domain.music.sheet.SongSheetRepository;
import com.wip.bool.domain.music.song.*;
import com.wip.bool.web.dto.music.song.SongDetailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Resource(name = "searchStoreProxy")
    private SearchStore searchStore;

    private final SongDetailRepository songDetailRepository;
    private final SongMasterRepository songMasterRepository;
    private final GuitarCodeRepository guitarCodeRepository;
    private final WordsMasterRepository wordsMasterRepository;
    private final SongSheetRepository songSheetRepository;
    private final SongMP3Repository songMP3Repository;

    @Transactional
    public Long save(SongDetailDto.SongDetailSaveRequest requestDto) {

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
    public Long update(Long songDetailId, SongDetailDto.SongDetailUpdateRequest requestDto) {

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

        Optional.ofNullable(requestDto.getLyrics()).ifPresent(lyrics -> songDetail.updateLyrics(lyrics));

        return songDetail.getId();
    }

    @Transactional
    public Long delete(Long songDetailId) {

        boolean isDeleteSheet = true;
        boolean isDeleteMP3 = true;

        SongDetail songDetail = selectedSongDetail(songDetailId);

        songDetailRepository.delete(songDetail);

        List<SongSheet> songSheets = songSheetRepository.findBySongDetail(songDetail);
        SongMP3 songMP3 = songMP3Repository.findBySongDetail(songDetail);

        if(songSheets.size() > 0) {
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
    public List<SongDetailDto.SongDetailSimpleResponse> gets(Long songMasterId, String order, String sort,
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

        return songDetailRepository.findAll(songMaster, sortType, orderType, offset, size);
    }

    @Transactional(readOnly = true)
    public SongDetailDto.SongDetailResponse get(Long songDetailId, Long userId) {
        return songDetailRepository.findById(songDetailId, userId)
                .orElseThrow(() -> new IllegalArgumentException("노래를 찾을 수 없습니다."));
    }

    public List<SongDetailDto.SongDetailSimpleResponse> search(String keyword) {
        return searchStore.findWords(keyword)
                .stream()
                .map(SongDetailDto.SongDetailSimpleResponse::new)
                .collect(Collectors.toList());
    }

    private SongDetail selectedSongDetail(Long songDetailId) {
        return songDetailRepository.findById(songDetailId)
                .orElseThrow(() -> new IllegalArgumentException("해당 곡이 존재하지 않습니다. id = " + songDetailId));
    }

    private WordsMaster selectedWordsMaster(Long wordsMasterId) {
        return wordsMasterRepository.findById(wordsMasterId)
                .orElseThrow(() -> new IllegalArgumentException("words master id가 존재하지 않습니다. "));
    }

    private GuitarCode selectedGuitarCode(Long guitarCodeId) {
        return guitarCodeRepository.findById(guitarCodeId)
                .orElseThrow(() -> new IllegalArgumentException("guitar code id가 존재하지 않습니다. "));
    }

    private SongMaster selectedSongMaster(Long songMasterId) {
        return  songMasterRepository.findById(songMasterId)
                .orElseThrow(() -> new IllegalArgumentException("code key가 존재하지 않습니다. "));
    }
}
