package com.wip.bool.cmmn.util;

import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private static DateTimeFormatter DF_YMD_HM = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");

    public static DateTimeFormatter YYYY_MM_DD_HH_mm() {
        return DF_YMD_HM;
    }
}
