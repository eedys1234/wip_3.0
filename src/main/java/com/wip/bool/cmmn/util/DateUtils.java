package com.wip.bool.cmmn.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtils {

    private static DateTimeFormatter DF_YMD_HM = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");

    public static DateTimeFormatter YYYY_MM_DD_HH_mm() {
        return DF_YMD_HM;
    }
}
