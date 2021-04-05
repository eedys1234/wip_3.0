package com.wip.bool.domain.music;

import com.wip.bool.domain.cmmn.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class SongSheet extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "sheet_order")
    private int sheetOrder;
}
