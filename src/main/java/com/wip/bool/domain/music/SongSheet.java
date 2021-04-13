package com.wip.bool.domain.music;

import com.wip.bool.domain.cmmn.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "song_sheet")
public class SongSheet extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "sheet_order")
    private int sheetOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_detail_id")
    private SongDetail songDetail;
}
