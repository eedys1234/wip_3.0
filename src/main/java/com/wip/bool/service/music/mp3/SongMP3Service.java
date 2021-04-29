package com.wip.bool.service.music.mp3;

import com.wip.bool.domain.music.song.SongDetail;
import com.wip.bool.domain.music.song.SongDetailRepository;
import com.wip.bool.domain.music.mp3.SongMP3;
import com.wip.bool.domain.music.mp3.SongMP3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SongMP3Service {

    @Value("${spring.mp3.path}")
    private String mp3FilePath;

    private final SongDetailRepository songDetailRepository;
    private final SongMP3Repository songMP3Repository;

    @Transactional
    public Long save(Long songDetailId, String orgFileName, byte[] mp3File) {

        SongDetail songDetail = songDetailRepository.findById(songDetailId)
                .orElseThrow(() -> new IllegalArgumentException("해당 곡이 존재하지 않습니다. id = " + songDetailId));

        SongMP3 songMP3 = SongMP3.createSongMP3(songDetail, mp3FilePath, orgFileName, mp3File);

        songMP3Repository.save(songMP3);

        if(!songMP3.createMP3File(mp3FilePath)) {
            throw new IllegalStateException("mp3 파일 생성이 실패했습니다.");
        }

        if(!songMP3.updateMP3Info(mp3FilePath)) {
            songMP3.deleteMP3File(mp3FilePath);

            throw new IllegalStateException("mp3 파일 생성이 실패했습니다.");
        }

        return songMP3.getId();
    }

    @Transactional
    public Long delete(Long songMP3Id) {

        SongMP3 songMP3 = songMP3Repository.findById(songMP3Id)
            .orElseThrow(() -> new IllegalArgumentException("MP3 파일이 존재하지 않습니다. id = " + songMP3Id));

        songMP3Repository.delete(songMP3);

        if(!songMP3.deleteMP3File(mp3FilePath)) {
            throw new IllegalStateException("mp3 파일 삭제가 실패했습니다.");
        }

        return 1L;
    }

    @Transactional(readOnly = true)
    public byte[] getFile(Long songMP3Id) {

        SongMP3 songMP3 = songMP3Repository.findById(songMP3Id)
                .orElseThrow(() -> new IllegalArgumentException("MP3 파일이 존재하지 않습니다. id = " + songMP3Id));

        return songMP3.getFile(mp3FilePath);
    }

}
