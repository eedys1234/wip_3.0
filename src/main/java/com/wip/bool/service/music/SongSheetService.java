package com.wip.bool.service.music;

import com.wip.bool.domain.music.song.SongDetail;
import com.wip.bool.domain.music.song.SongDetailRepository;
import com.wip.bool.domain.music.sheet.SongSheet;
import com.wip.bool.domain.music.sheet.SongSheetRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("해당 곡이 존재하지 않습니다. id = " + songDetailId));

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
            .orElseThrow(()->new IllegalArgumentException("악보가 존재하지 않습니다. id = " + songSheetId));

        Long resValue = songSheetRepository.delete(songSheet);
        if(!songSheet.deleteSheetFile(imageFilePath)) {
            throw new IllegalStateException("악보파일 삭제가 실패했습니다.");
        }

        return resValue;
    }

}
