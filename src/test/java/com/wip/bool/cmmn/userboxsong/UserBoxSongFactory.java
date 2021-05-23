package com.wip.bool.cmmn.userboxsong;

import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.userbox.domain.UserBox;
import com.wip.bool.userbox.domain.UserBoxSong;
import org.springframework.test.util.ReflectionTestUtils;

public class UserBoxSongFactory {

    public static UserBoxSong getUserBoxSong(SongDetail songDetail, UserBox userBox) {
        UserBoxSong userBoxSong = UserBoxSong.createUserBoxSong(songDetail, userBox);
        return userBoxSong;
    }

    public static UserBoxSong getUserBoxSong(SongDetail songDetail, UserBox userBox, Long id) {
        UserBoxSong userBoxSong = UserBoxSong.createUserBoxSong(songDetail, userBox);
        ReflectionTestUtils.setField(userBoxSong, "id", id);
        return userBoxSong;
    }
}
