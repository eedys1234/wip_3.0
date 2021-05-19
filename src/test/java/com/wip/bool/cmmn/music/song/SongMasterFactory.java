package com.wip.bool.cmmn.music.song;

import com.wip.bool.music.song.domain.SongMaster;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SongMasterFactory {

    public static String[] codeNames = {
        "말씀의 노래",
        "찬송의 노래",
        "하나님의 사람들의 노래",
        "예배의 노래",
        "교회의 노래",
        "메시지의 노래",
        "고백의 노래",
        "우리들의 노래",
        "친구들의 노래",
        "10대의 노래",
        "찬송이 아닌 노래들"
    };

    public static List<String> getCodeNames() {
        return Arrays.stream(codeNames)
                    .collect(Collectors.toList());
    }

    public  static List<SongMaster> getSongMasters() {
        AtomicInteger index = new AtomicInteger(1);
        return Arrays.stream(codeNames)
                .map(codeName -> {
                    SongMaster songMaster = getSongMaster(codeName, index.intValue());
                    index.incrementAndGet();
                    return songMaster;
                })
                .collect(Collectors.toList());
    }

    public static List<SongMaster> getSongMastersWithId() {

        AtomicInteger index = new AtomicInteger(1);
        return Arrays.stream(codeNames)
                .map(codeName -> {
                    SongMaster songMaster = getSongMaster(codeName, index.intValue(), index.intValue());
                    index.incrementAndGet();
                    return songMaster;
                })
                .collect(Collectors.toList());
    }

    public static SongMaster getSongMaster() {
        return getSongMaster("하나님의 사랑");
    }

    public static SongMaster getSongMaster(long id) {
        SongMaster songMaster = getSongMaster("하나님의 사랑");
        ReflectionTestUtils.setField(songMaster, "id", id);
        return songMaster;
    }

    public static SongMaster getSongMaster(String codeName) {
        return getSongMaster(codeName, 1);
    }

    public static SongMaster getSongMaster(String codeName, long id) {
        SongMaster songMaster = getSongMaster(codeName, 1);
        ReflectionTestUtils.setField(songMaster, "id", id);
        return songMaster;
    }

    public static SongMaster getSongMaster(String codeName, int codeOrder) {
        SongMaster songMaster = SongMaster.createSongMaster(codeName, codeOrder);
        return songMaster;
    }

    public static SongMaster getSongMaster(String codeName, int codeOrder, long id) {
        SongMaster songMaster = getSongMaster(codeName, codeOrder);
        ReflectionTestUtils.setField(songMaster, "id", id);
        return songMaster;
    }
}
