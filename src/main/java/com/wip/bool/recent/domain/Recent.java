package com.wip.bool.recent.domain;

import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "recent")
public class Recent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recent_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_detail_id")
    private SongDetail songDetail;

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;

    public static Recent createRecent(SongDetail songDetail, User user) {
        Recent recent = new Recent();
        recent.updateSongDetail(songDetail);
        recent.updateUser(user);
        return recent;
    }

    public void updateUser(User user) {
        this.user = user;
    }

    public void updateSongDetail(SongDetail songDetail) {
        this.songDetail = songDetail;
    }
}
