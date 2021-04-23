package com.wip.bool.domain.user;

import com.wip.bool.domain.music.SongDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "bookmark")
public class BookMark {

    @Id
    @GeneratedValue
    @Column(name = "bookmark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_detail_id")
    private SongDetail songDetail;

    private LocalDateTime createDate;

}
