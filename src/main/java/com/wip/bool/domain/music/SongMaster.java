package com.wip.bool.domain.music;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "song_master")
public class SongMaster {

    @Id
    @GeneratedValue
    @Column(name = "song_master_id")
    private Long id;

    @Column(name = "code_key", length = 32, nullable = false)
    private String codeKey;

    @Column(name = "code_name", length = 50, nullable = false)
    private String codeName;

    @Column(name = "code_order")
    private int codeOrder;

    @OneToMany(mappedBy = "songMaster")
    List<SongDetail> songDetails = new ArrayList<>();
}
