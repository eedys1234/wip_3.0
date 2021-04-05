package com.wip.bool.domain.user;

import com.wip.bool.domain.music.SongDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class RecentSearch {

    @Id
    @GeneratedValue
    @Column(name = "recent_search_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "song_detail_id")
    private SongDetail songDetail;

    private LocalDateTime createDate;
}
