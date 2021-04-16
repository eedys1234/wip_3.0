package com.wip.bool.service.music;

import com.wip.bool.domain.bible.WordsMaster;
import com.wip.bool.domain.bible.WordsMasterRepository;
import com.wip.bool.domain.cmmn.file.FileManager;
import com.wip.bool.domain.music.*;
import com.wip.bool.web.dto.music.SongDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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

    @Transactional
    public Long save(SongDto.SongDetailSaveRequest requestDto) {

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
    public Long update(Long songDetailId, SongDto.SongDetailUpdateRequest requestDto) {

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

    public Long delete(Long songDetailId) {

        SongDetail songDetail = songDetailRepository.findById(songDetailId)
                .orElseThrow(() -> new IllegalArgumentException("해당 곡이 존재하지 않습니다. id = " + songDetailId));

        List<SongSheet> songSheets = songSheetRepository.findBySongDetail(songDetail);

        //TODO : 람다식 안에 있는 로직을 함수로 분리시키는건 ???
        boolean isDelete = songSheets.stream().allMatch(sheet -> {
            try {
                return FileManager.delete(filePath, sheet.getSheetPath());
            } catch (IOException e) {
                log.error(String.format("파일 삭제 실패 : [ %s ]", filePath + sheet.getSheetPath()));
            }
            return false;
        });

        if(isDelete) {
            return delete(songDetail);
        }

        return -1L;
    }

    @Transactional
    private Long delete(SongDetail songDetail) {
        return songDetailRepository.delete(songDetail);
    }

}
