package com.wip.bool.domain.music;

import com.wip.bool.domain.cmmn.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "song_mp3")
public class SongMP3 extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private int duration;

    private int miliDuration;

    @Column(name = "mp3_path")
    private String mp3Path;
}
