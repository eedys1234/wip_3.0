package com.wip.bool.music.song.domain;

import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.cmmn.util.BaseEntity;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.mp3.domain.SongMP3;
import com.wip.bool.music.sheet.domain.SongSheet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "song_detail")
public class SongDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_detail_id")
    private Long id;

    @Column(name = "song_title", nullable = false, unique = true, length = 30)
    private String title;

    @Column(name = "song_lyrics", columnDefinition = "TEXT", nullable = false)
    private String lyrics;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_master_id", nullable = false)
    private SongMaster songMaster;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guitar_id", nullable = false)
    private GuitarCode guitarCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "words_master_id", nullable = false)
    private WordsMaster wordsMaster;

    @OneToMany(mappedBy = "songDetail")
    private List<SongSheet> songSheets = new ArrayList<>();

    @OneToOne(mappedBy = "songDetail", fetch = FetchType.LAZY)
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
        if(!StringUtils.isEmpty(this.title) && !this.title.equals(title)) {
            this.title = title;
        }
        else if(StringUtils.isEmpty(this.title)) {
            this.title = title;
        }
    }

    public void updateLyrics(String lyrics) {
        if(!StringUtils.isEmpty(this.lyrics) && !this.lyrics.equals(lyrics)) {
            this.lyrics = lyrics;
        }
        else if(StringUtils.isEmpty(this.lyrics)) {
            this.lyrics = lyrics;
        }
    }

    public void updateSongMaster(SongMaster songMaster) {
        if (Objects.isNull(this.songMaster)) {
            this.songMaster = songMaster;
        }
        else if(!this.songMaster.getId().equals(songMaster.getId())) {
            this.songMaster = songMaster;
        }
    }

    public void updateGuitarCode(GuitarCode guitarCode) {
        if (Objects.isNull(this.guitarCode)) {
            this.guitarCode = guitarCode;
        }
        else if(!this.guitarCode.getId().equals(guitarCode.getId())) {
            this.guitarCode = guitarCode;
        }
    }

    public void updateWordsMaster(WordsMaster wordsMaster) {
        if (Objects.isNull(this.wordsMaster)) {
            this.wordsMaster = wordsMaster;
        }
        else if(!this.wordsMaster.getId().equals(wordsMaster.getId())) {
            this.wordsMaster = wordsMaster;
        }
    }


}
