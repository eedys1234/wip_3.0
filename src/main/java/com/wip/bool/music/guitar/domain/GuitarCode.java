package com.wip.bool.music.guitar.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "guitar_codes")
public class GuitarCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guitar_id")
    private Long id;

    @Column(name = "guitar_code", nullable = false, length = 4, unique = true)
    private String code;

    @Column(name = "guitar_order", nullable = false)
    private int guitarOrder;

    public static GuitarCode createGuitarCode(String code, int guitarOrder) {
        GuitarCode guitarCode = new GuitarCode();
        guitarCode.updateCode(code);
        guitarCode.updateGuitarOrder(guitarOrder);
        return guitarCode;
    }

    public void updateCode(String code) {
        this.code = code;
    }

    public void updateGuitarOrder(int guitarOrder) {
        this.guitarOrder = guitarOrder;
    }
}
