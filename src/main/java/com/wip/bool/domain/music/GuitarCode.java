package com.wip.bool.domain.music;

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
    @GeneratedValue
    @Column(name = "guitar_id")
    private Long id;

    @Column(name = "guitar_code")
    private String code;

    @Column(name = "guitar_order")
    private int guitarOrder;

//    @OneToOne(mappedBy = "guitarCode", fetch = FetchType.LAZY)
//    private SongDetail songDetail;

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
