package com.wip.bool.service.music;

import com.wip.bool.domain.bible.WordsMaster;
import com.wip.bool.domain.bible.WordsMasterRepository;
import com.wip.bool.domain.cmmn.page.CustomPageRequest;
import com.wip.bool.domain.music.*;
import com.wip.bool.web.dto.music.SongDetailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SongDetailService {

    @Value("spring.images.path")
    private String filePath;

    private final SongDetailRepository songDetailRepository;
    private final SongMasterRepository songMasterRepository;
    private final GuitarCodeRepository guitarCodeRepository;
    private final WordsMasterRepository wordsMasterRepository;
    private final SongSheetRepository songSheetRepository;
    private final SongMP3Repository songMP3Repository;

    @Transactional
    public Long save(SongDetailDto.SongDetailSaveRequest requestDto) {

        SongMaster songMaster = songMasterRepository.findById(requestDto.getCodeKey())
                .orElseThrow(() -> new IllegalArgumentException("code key가 존재하지 않습니다. "));

        GuitarCode guitarCode = guitarCodeRepository.findById(requestDto.getGuitarCodeKey())
                .orElseThrow(() -> new IllegalArgumentException("guitar code id가 존재하지 않습니다. "));

        WordsMaster wordsMaster = wordsMasterRepository.findById(requestDto.getWordsMasterKey())
                .orElseThrow(() -> new IllegalArgumentException("words master id가 존재하지 않습니다. "));

        SongDetail songDetail = SongDetail.createSongDetail(requestDto.getTitle(), requestDto.getLyrics(), songMaster,
                guitarCode, wordsMaster);

        return songDetailRepository.save(songDetail).getId();
    }

    @Transactional
    public Long update(Long songDetailId, SongDetailDto.SongDetailUpdateRequest requestDto) {

        SongDetail songDetail = songDetailRepository.findById(songDetailId)
                .orElseThrow(() -> new IllegalArgumentException("해당 곡이 존재하지 않습니다. id = " + songDetailId));

        if(!Objects.isNull(requestDto.getCodeKey())) {
            SongMaster songMaster = songMasterRepository.findById(requestDto.getCodeKey())
                    .orElseThrow(() -> new IllegalArgumentException("code key가 존재하지 않습니다. "));

            songDetail.updateSongMaster(songMaster);
        }

        if(!Objects.isNull(requestDto.getGuitarCodeKey())) {
            GuitarCode guitarCode = guitarCodeRepository.findById(requestDto.getGuitarCodeKey())
                    .orElseThrow(() -> new IllegalArgumentException("guitar code id가 존재하지 않습니다. "));

            songDetail.updateGuitarCode(guitarCode);
        }

        if(!Objects.isNull(requestDto.getWordsMasterKey())) {
            WordsMaster wordsMaster = wordsMasterRepository.findById(requestDto.getWordsMasterKey())
                    .orElseThrow(() -> new IllegalArgumentException("words master id가 존재하지 않습니다. "));

            songDetail.updateWordsMaster(wordsMaster);
        }

        if(!StringUtils.isEmpty(requestDto.getTitle())) {
            songDetail.updateTitle(requestDto.getTitle());
        }

        if(!StringUtils.isEmpty(requestDto.getLyrics())) {
            songDetail.updateTitle(requestDto.getLyrics());
        }

        //TODO : update 메소드만으로 업데이트 칠수 있을지 테스트 필요
        return songDetailRepository.save(songDetail).getId();
    }

    @Transactional
    public Long delete(Long songDetailId) {

        boolean isDeleteSheet = true;
        boolean isDeleteMP3 = true;

        SongDetail songDetail = songDetailRepository.findById(songDetailId)
                .orElseThrow(() -> new IllegalArgumentException("해당 곡이 존재하지 않습니다. id = " + songDetailId));

        songDetailRepository.delete(songDetail);

        List<SongSheet> songSheets = songSheetRepository.findBySongDetail(songDetail);
        SongMP3 songMP3 = songMP3Repository.findBySongDetail(songDetail);

        if(songSheets.size() > 0) {
            isDeleteSheet = songSheets.stream().allMatch(sheet -> sheet.deleteSheetFile(filePath));
            if(!isDeleteSheet) throw new IllegalStateException("악보파일 삭제가 실패했습니다.");
        }

        if(!Objects.isNull(songMP3)) {
            isDeleteMP3 = songMP3.deleteMP3File(filePath);
            if(!isDeleteMP3) throw new IllegalStateException("MP3파일 삭제가 실패했습니다.");
        }

        return 1L;
    }

    @Transactional(readOnly = true)
    public List<SongDetailDto.SongDetailResponse> gets(SongDetailDto.SongDetailsRequest requestDto,
                                                       int size, int page) {

        CustomPageRequest pageRequest = CustomPageRequest.builder()
                .size(size)
                .page(page)
                .build();

        SongMaster songMaster = null;
        if(!Objects.isNull(requestDto.getSongMasterId())) {
            songMaster = songMasterRepository.findById(requestDto.getSongMasterId())
                    .orElseThrow(() -> new IllegalArgumentException());
        }

        SortType sortType = SortType.valueOf(requestDto.getSortType());
        OrderType orderType = OrderType.valueOf(requestDto.getOrder());

        if(Objects.isNull(sortType) || Objects.isNull(orderType)) {
            throw new IllegalArgumentException();
        }

        return songDetailRepository.findAll(songMaster, sortType, orderType, pageRequest.of())
                .stream()
                .map(SongDetailDto.SongDetailResponse::new)
                .collect(Collectors.toList());
    }


}
