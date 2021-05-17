package com.wip.bool.music.sheet.service;

import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.music.sheet.domain.SongSheet;
import com.wip.bool.music.sheet.domain.SongSheetRepository;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SongSheetService {

    @Value("${spring.images.path}")
    private String imageFilePath;

    private final SongSheetRepository songSheetRepository;
    private final SongDetailRepository songDetailRepository;

    @Transactional
    public Long save(Long songDetailId, String orgFileName, byte[] imageFiles) {

        SongDetail songDetail = songDetailRepository.findById(songDetailId)
                .orElseThrow(() -> new EntityNotFoundException(songDetailId, ErrorCode.NOT_FOUND_SONG));

        SongSheet songSheet = SongSheet.createSongSheet(songDetail, imageFilePath, orgFileName, imageFiles, songDetail.getSongSheets().size() + 1);

        songSheetRepository.save(songSheet);

        if(!songSheet.createSheetFile(imageFilePath)) {
            throw new IllegalStateException("악보파일 생성이 실패했습니다.");
        }

        return songSheet.getId();
    }

    @Transactional
    public Long delete(Long songSheetId) {

        SongSheet songSheet = songSheetRepository.findById(songSheetId)
            .orElseThrow(() -> new EntityNotFoundException(songSheetId, ErrorCode.NOT_FOUND_SHEET));

        Long resValue = songSheetRepository.delete(songSheet);
        if(!songSheet.deleteSheetFile(imageFilePath)) {
            throw new IllegalStateException("악보파일 삭제가 실패했습니다.");
        }

        return resValue;
    }

}
