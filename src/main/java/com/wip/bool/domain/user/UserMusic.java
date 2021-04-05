package com.wip.bool.domain.user;

import com.wip.bool.domain.music.SongDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class UserMusic {

    @Id
    @GeneratedValue
    @Column(name = "user_music_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_detail_id")
    private List<SongDetail> songDetails = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_box_id")
    private UserBox userBox;

}
