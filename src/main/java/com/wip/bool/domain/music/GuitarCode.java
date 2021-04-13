package com.wip.bool.domain.music;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "guitar_code")
public class GuitarCode {

    @Id
    @GeneratedValue
    @Column(name = "guitar_id")
    private Long id;

    @Column(name = "guitar_code")
    private String code;

    @Column(name = "guitar_order")
    private int guitarOrder;

}
