package com.wip.bool.cmmn.calendar;

import com.wip.bool.calendar.domain.Calendar;
import com.wip.bool.cmmn.type.ShareType;
import com.wip.bool.user.domain.User;

import java.time.LocalDateTime;

public class CalendarFactory {

    public static Calendar getPublicCalendar(User user) {

        String title = "일정 추가!";
        String content = "네트워크 일정";
        ShareType shareType = ShareType.PUBLIC;
        LocalDateTime date = LocalDateTime.of(2021, 05, 02, 16, 00, 00);

        Calendar calendar = Calendar.createCalender(title, content, date, shareType, user);
        return calendar;
    }
}
