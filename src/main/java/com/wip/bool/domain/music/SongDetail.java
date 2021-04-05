package com.wip.bool.domain.music;

import com.wip.bool.domain.bible.WordsMaster;
import com.wip.bool.domain.cmmn.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class SongDetail extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "song_detail_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String lyics;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_key")
    private SongMaster songMaster;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guitar_id")
    private GuitarCode guitarCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "words_master_id")
    private WordsMaster wordsMaster;

    @OneToMany(mappedBy = "songDetail")
    private List<SongSheet> songSheets = new ArrayList<>();

    @Column(name = "music_key")
    private String musicKey;
}
