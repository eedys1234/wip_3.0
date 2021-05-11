package com.wip.bool.cmmn.song.guitarcode;

import com.wip.bool.music.guitar.domain.GuitarCode;

public class GuitarCodeFactory {

    public static GuitarCode getGuitarCode() {

        String code = "C";
        int order = 1;
        GuitarCode guitarCode = GuitarCode.createGuitarCode(code, order);
        return guitarCode;
    }
}
