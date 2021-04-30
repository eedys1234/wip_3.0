package com.wip.bool.music.song.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "song_master")
public class SongMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_master_id")
    private Long id;

    @Column(name = "code_key", length = 32, nullable = false, unique = true)
    private String codeKey;

    @Column(name = "code_name", length = 10, nullable = false)
    private String codeName;

    @Column(name = "code_order", nullable = false)
    private int codeOrder;

    @OneToMany(mappedBy = "songMaster")
    List<SongDetail> songDetails = new ArrayList<>();

    public static SongMaster createSongMaster(String codeName, int codeOrder) {

        SongMaster songMaster = new SongMaster();
        songMaster.updateCodeKey();
        songMaster.updateCodeName(codeName);
        songMaster.updateCodeOrder(codeOrder);
        return songMaster;
    }

    private void updateCodeKey() {
        this.codeKey = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .toUpperCase();
    }

    public void updateCodeName(String codeName) {
        this.codeName = codeName;
    }

    public void updateCodeOrder(int codeOrder) {
        this.codeOrder = codeOrder;
    }
}
