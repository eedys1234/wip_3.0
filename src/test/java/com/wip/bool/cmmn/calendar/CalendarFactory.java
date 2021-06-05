package com.wip.bool.cmmn.calendar;

import com.wip.bool.calendar.repository.Calendar;
import com.wip.bool.cmmn.type.ShareType;
import com.wip.bool.user.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CalendarFactory {

    public static List<Calendar> getDeptCalendars(User user) {

        return Arrays.asList(
                getDeptCalendar(user, "OO부서 회의", "모니터링 시스템 검토", 1L),
                getDeptCalendar(user, "OO부서 회의 - 1", "모니터링 시스템 검토", 2L),
                getDeptCalendar(user, "OO부서 회의 - 2", "모니터링 시스템 검토", 3L)
        );
    }

    public static List<Calendar> getIndividualCalendars(User user) {

        return Arrays.asList(
                getPrivateCalendar(user,"OO부서 회의", "모니터링 시스템 검토", 1L),
                getPrivateCalendar(user,"OO부서 회의 - 1", "모니터링 시스템 검토", 2L),
                getPrivateCalendar(user,"OO부서 회의 - 2", "모니터링 시스템 검토", 3L)
        );
    }

    public static Calendar getPublicCalendar(User user) {

        String title = "일정 추가!";
        String content = "네트워크 일정";
        ShareType shareType = ShareType.PUBLIC;
        LocalDateTime date = LocalDateTime.now().minusDays(1);

        Calendar calendar = Calendar.createCalender(title, content, date, shareType, user);
        return calendar;
    }


    public static Calendar getPublicCalendar(User user, long id) {
        Calendar calendar = getPublicCalendar(user);
        ReflectionTestUtils.setField(calendar, "id", id);
        return calendar;
    }

    public static Calendar getDeptCalendar(User user, String title, String content) {

        ShareType shareType = ShareType.DEPT;
        LocalDateTime date = LocalDateTime.now();

        Calendar calendar = Calendar.createCalender(title, content, date, shareType, user);
        return calendar;
    }

    public static Calendar getDeptCalendar(User user, String title, String content, long id) {
        Calendar deptCalendar = getDeptCalendar(user, title, content);
        ReflectionTestUtils.setField(deptCalendar, "id", id);
        return deptCalendar;
    }

    public static Calendar getPrivateCalendar(User user, String title, String content) {

        ShareType shareType = ShareType.PRIVATE;
        LocalDateTime date = LocalDateTime.now();

        Calendar calendar = Calendar.createCalender(title, content, date, shareType, user);
        return calendar;
    }

    public static Calendar getPrivateCalendar(User user, String title, String content, long id) {
        Calendar privateCalendar = getPrivateCalendar(user, title, content);
        ReflectionTestUtils.setField(privateCalendar, "id", id);
        return privateCalendar;
    }
}
