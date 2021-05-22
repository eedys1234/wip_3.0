package com.wip.bool.cmmn.music.guitarcode;

import com.wip.bool.music.guitar.domain.GuitarCode;
import org.springframework.test.util.ReflectionTestUtils;

public class GuitarCodeFactory {

    public static GuitarCode getGuitarCode() {
        String code = "C";
        int order = 1;
        GuitarCode guitarCode = getGuitarCode(code, order);
        return guitarCode;
    }

    public static GuitarCode getGuitarCode(Long id) {
        GuitarCode guitarCode = getGuitarCode();
        ReflectionTestUtils.setField(guitarCode, "id", id);
        return guitarCode;
    }

    public static GuitarCode getGuitarCode(String code) {
        GuitarCode guitarCode = getGuitarCode(code, 1);
        return guitarCode;
    }

    public static GuitarCode getGuitarCode(String code, Long id) {
        GuitarCode guitarCode = getGuitarCode(code, 1);
        ReflectionTestUtils.setField(guitarCode, "id", id);
        return guitarCode;
    }

    public static GuitarCode getGuitarCode(String code, int order) {
        GuitarCode guitarCode = GuitarCode.createGuitarCode(code, order);
        return guitarCode;
    }

    public static GuitarCode getGuitarCode(String code, int order, Long id) {
        GuitarCode guitarCode = getGuitarCode(code, order);
        ReflectionTestUtils.setField(guitarCode, "id", id);
        return guitarCode;
    }

}
