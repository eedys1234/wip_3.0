package com.wip.bool.domain.music;

import com.wip.bool.domain.bible.WordsMaster;
import com.wip.bool.domain.cmmn.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "song_detail")
public class SongDetail extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "song_detail_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String lyrics;

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

    @OneToOne(fetch = FetchType.LAZY)
    private SongMP3 songMP3;

    public static SongDetail createSongDetail(String title, String lyrics, SongMaster songMaster, GuitarCode guitarCode,
                                              WordsMaster wordsMaster) {

        SongDetail songDetail = new SongDetail();
        songDetail.updateTitle(title);
        songDetail.updateLyrics(lyrics);
        songDetail.updateSongMaster(songMaster);
        songDetail.updateGuitarCode(guitarCode);
        songDetail.updateWordsMaster(wordsMaster);

        return songDetail;
    }

    public void updateTitle(String title) {
        if(!StringUtils.isEmpty(title) && this.title != title) {
            this.title = title;
        }
    }

    public void updateLyrics(String lyrics) {
        if(this.lyrics != lyrics) {
            this.lyrics = lyrics;
        }
    }

    public void updateSongMaster(SongMaster songMaster) {
        if(this.songMaster != songMaster) {
            this.songMaster = songMaster;
        }
    }

    public void updateGuitarCode(GuitarCode guitarCode) {
        if(this.guitarCode != guitarCode) {
            this.guitarCode = guitarCode;
        }
    }

    public void updateWordsMaster(WordsMaster wordsMaster) {
        if(this.wordsMaster != wordsMaster) {
            this.wordsMaster = wordsMaster;
        }
    }

//    @Column(name = "music_key")
//    private String musicKey;
}