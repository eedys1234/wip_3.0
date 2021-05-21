package com.wip.bool.cmmn.music.sheet;

import com.wip.bool.music.sheet.domain.SongSheet;
import com.wip.bool.music.song.domain.SongDetail;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SongSheetFactory {

    public static List<SongSheet> getSongSheets(SongDetail songDetail, String filePath, String orgFileName, byte[]... imagesFile) {
        List<SongSheet> songSheets = new ArrayList<>();
        AtomicInteger index = new AtomicInteger(1);

        for (byte[] bytes : imagesFile) {
            songSheets.add(getSongSheet(songDetail, filePath, orgFileName, bytes, index.intValue(), index.longValue()));
            index.incrementAndGet();
        }

        return songSheets;
    }

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
