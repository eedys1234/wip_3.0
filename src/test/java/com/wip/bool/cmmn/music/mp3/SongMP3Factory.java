package com.wip.bool.cmmn.music.mp3;

import com.wip.bool.music.mp3.domain.SongMP3;
import com.wip.bool.music.song.domain.SongDetail;
import org.springframework.test.util.ReflectionTestUtils;

public class SongMP3Factory {

    public static SongMP3 getSongMP3(SongDetail songDetail, String filePath, String orgFileName, byte[] mp3File) {
        SongMP3 songMP3 = SongMP3.createSongMP3(songDetail, filePath, orgFileName, mp3File);
        return songMP3;
    }

    public static SongMP3 getSongMP3(SongDetail songDetail, String filePath, String orgFileName, byte[] mp3File, Long id) {
        SongMP3 songMP3 = getSongMP3(songDetail, filePath, orgFileName, mp3File);
        ReflectionTestUtils.setField(songMP3, "id", id);
        return songMP3;
    }
}
