package com.wip.bool.cmmn.recent;

import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.recent.domain.Recent;
import com.wip.bool.user.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RecentFactory {

    public static List<Recent> getRcents(User user, List<SongDetail> songDetails) {
        return songDetails.stream().map(songDetail -> getRecent(user, songDetail)).collect(Collectors.toList());
    }

    public static List<Recent> getRcentsWithId(User user, List<SongDetail> songDetails) {
        AtomicInteger index = new AtomicInteger(0);
        return songDetails.stream().map(songDetail -> getRecent(user, songDetail, index.incrementAndGet())).collect(Collectors.toList());
    }

    public static Recent getRecent(User user, SongDetail songDetail) {
        Recent recent = Recent.createRecent(songDetail, user);
        return recent;
    }

    public static Recent getRecent(User user, SongDetail songDetail, long id) {
        Recent recent = getRecent(user, songDetail);
        ReflectionTestUtils.setField(recent, "id", id);
        return recent;
    }

}
