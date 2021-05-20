package com.wip.bool.cmmn.music.sheet;

import com.wip.bool.music.sheet.domain.SongSheet;
import com.wip.bool.music.song.domain.SongDetail;
import org.springframework.test.util.ReflectionTestUtils;

public class SongSheetFactory {

    public static SongSheet getSongSheet(SongDetail songDetail, String filePath, String orgFileName, byte[] imagesFile, int sheetOrder) {
        SongSheet songSheet = SongSheet.createSongSheet(songDetail, filePath, orgFileName, imagesFile, sheetOrder);
        return songSheet;
    }

    public static SongSheet getSongSheet(SongDetail songDetail, String filePath, String orgFileName, byte[] imagesFile, int sheetOrder, Long id) {
        SongSheet songSheet = getSongSheet(songDetail, filePath, orgFileName, imagesFile, sheetOrder);
        ReflectionTestUtils.setField(songSheet, "id", id);
        return songSheet;
    }
}
