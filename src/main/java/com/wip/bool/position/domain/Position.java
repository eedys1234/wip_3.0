package com.wip.bool.position.domain;

import com.wip.bool.cmmn.util.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "positions")
public class Position extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Long id;

    @Column(name = "position_name", unique = true, nullable = false, length = 4)
    private String positionName;

    @Builder
    public Position(String positionName) {
        this.positionName = positionName;
    }

    public void updatePositionName(String positionName) {

        if(!Objects.isNull(positionName)) {
            this.positionName = positionName;
        }
    }
}
