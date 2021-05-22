package com.wip.bool.cmmn.music.song;

import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongMaster;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SongDetailFactory {

    public static String[] titles = {
        "우리에게 이 모든 선물을 주신 것은",
        "다시 생각해도 눈물 기억이야 (성덕송)",
        "오늘도 벅찬 마음으로 당신께 나아갑니다",
        "주의 이름을 부르는 자는 구원을 받으리라",
        "믿음 안에 서 있는 나는 건강하네",
        "온 땅은 여호와를 경외할지어다",
        "지혜와 계시 부으소서",
        "말씀을 몇 번 들어야 나는 달라질까",
        "나는 내가 하는 일을 잘 모르겠어요",
        "내가 매일 기쁘게",
    };

    public static String[] lyrics = {
        "에베소서 4:11-16 (14절 제외, 쉬운 ver)",
        "내 어릴 적 소풍을 나는 기억하네",
        "주님 오늘도 벅찬 마음으로 나 당신께 나아갑니다",
        "사도행전 2:17-21",
        "내 삶의 고통도",
        "시편 33편 1절 ~ 9절, 12",
        "지혜와 계시",
        "말씀을 들을 때마다 나를 돌아봐야지",
        "어김 없이 찾아오는",
        "내가 매일 기쁘게 순례의 길 행함은"
    };

    public static List<String> getSongDetailTitle() {
        return Arrays.stream(titles).collect(Collectors.toList());
    }

    public static List<SongDetail> getSongDetails(SongMaster songMaster, GuitarCode guitarCode, WordsMaster wordsMaster) {
        List<SongDetail> songDetails = new ArrayList<>();

        for(int i=0;i<titles.length;i++)
        {
            songDetails.add(getSongDetail(titles[i], lyrics[i], songMaster, guitarCode, wordsMaster));
        }

        return songDetails;
    }

    public static List<SongDetail> getSongDetailsWithId(SongMaster songMaster, GuitarCode guitarCode, WordsMaster wordsMaster) {
        List<SongDetail> songDetails = new ArrayList<>();

        for(int i=1;i<=titles.length;i++)
        {
            songDetails.add(getSongDetail(titles[i-1], lyrics[i-1], songMaster, guitarCode, wordsMaster, (long)i));
        }

        return songDetails;
    }

    public static SongDetail getSongDetail(SongMaster songMaster, GuitarCode guitarCode, WordsMaster wordsMaster) {

        String title = "우리에게 이 모든 선물을 주신 것은";
        String lyrics = "에베소서 4:11-16 (14절 제외, 쉬운 ver)\n";
        SongDetail songDetail = SongDetail.createSongDetail(title, lyrics, songMaster, guitarCode, wordsMaster);
        return songDetail;
    }

    public static SongDetail getSongDetail(String title, String lyrics, SongMaster songMaster, GuitarCode guitarCode, WordsMaster wordsMaster) {
        SongDetail songDetail = SongDetail.createSongDetail(title, lyrics, songMaster, guitarCode, wordsMaster);
        return songDetail;
    }

    public static SongDetail getSongDetail(SongMaster songMaster, GuitarCode guitarCode, WordsMaster wordsMaster, Long id) {
        SongDetail songDetail = getSongDetail(songMaster, guitarCode, wordsMaster);
        ReflectionTestUtils.setField(songDetail, "id", id);
        return songDetail;
    }

    public static SongDetail getSongDetail(String title, String lyrics, SongMaster songMaster, GuitarCode guitarCode, WordsMaster wordsMaster, Long id) {
        SongDetail songDetail = SongDetail.createSongDetail(title, lyrics, songMaster, guitarCode, wordsMaster);
        ReflectionTestUtils.setField(songDetail, "id", id);
        return songDetail;
    }
}
