package com.wip.bool.bookmark.domain;

import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "bookmark")
public class BookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_detail_id")
    private SongDetail songDetail;

    @CreatedDate
    private LocalDateTime createDate;

    public static BookMark createBookMark(User user, SongDetail songDetail) {
        BookMark bookMark = new BookMark();
        bookMark.updateUser(user);
        bookMark.updateSongDetail(songDetail);
        return bookMark;
    }

    public void updateUser(User user) {
        this.user = user;
    }

    public void updateSongDetail(SongDetail songDetail) {
        this.songDetail = songDetail;
    }


}
