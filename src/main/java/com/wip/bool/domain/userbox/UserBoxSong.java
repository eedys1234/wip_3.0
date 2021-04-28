package com.wip.bool.domain.userbox;

import com.wip.bool.domain.music.song.SongDetail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_music")
public class UserBoxSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_box_song_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_detail_id")
    private SongDetail songDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_box_id")
    private UserBox userBox;

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;

}
