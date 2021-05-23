package com.wip.bool.cmmn.calendar;

import com.wip.bool.calendar.dto.CalendarDto;
import com.wip.bool.calendar.repository.Calendar;
import com.wip.bool.cmmn.type.ShareType;
import com.wip.bool.user.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CalendarFactory {

    public static List<CalendarDto.CalendarResponse> getDeptCalendars() {

        List<CalendarDto.CalendarResponse> deptCalendars = Arrays.asList(
                new CalendarDto.CalendarResponse(1L, "OO부서 회의", "모니터링 시스템 검토", LocalDateTime.of(2021, 05, 01, 14, 00, 00),
                        ShareType.DEPT, "test@gmail.com", 1L),
                new CalendarDto.CalendarResponse(2L, "OO부서 회의 - 1", "모니터링 시스템 검토", LocalDateTime.of(2021, 05, 02, 14, 00, 00),
                        ShareType.PUBLIC, "test2@gmail.com", 1L),
                new CalendarDto.CalendarResponse(3L, "OO부서 회의 - 2", "모니터링 시스템 검토", LocalDateTime.of(2021, 05, 03, 14, 00, 00),
                        ShareType.DEPT, "test2@gmail.com", 1L)
        );

        return deptCalendars;
    }

    public static List<CalendarDto.CalendarResponse> getIndividualCalendars() {

        List<CalendarDto.CalendarResponse> deptCalendars = Arrays.asList(
                new CalendarDto.CalendarResponse(1L, "OO부서 회의", "모니터링 시스템 검토", LocalDateTime.of(2021, 05, 01, 14, 00, 00),
                        ShareType.PRIVATE, "test@gmail.com", 1L),
                new CalendarDto.CalendarResponse(2L, "OO부서 회의 - 1", "모니터링 시스템 검토", LocalDateTime.of(2021, 05, 02, 14, 00, 00),
                        ShareType.PRIVATE, "test2@gmail.com", 1L),
                new CalendarDto.CalendarResponse(3L, "OO부서 회의 - 2", "모니터링 시스템 검토", LocalDateTime.of(2021, 05, 03, 14, 00, 00),
                        ShareType.PRIVATE, "test2@gmail.com", 1L)
        );

        return deptCalendars;
    }

    public static Calendar getPublicCalendar(User user) {

        String title = "일정 추가!";
        String content = "네트워크 일정";
        ShareType shareType = ShareType.PUBLIC;
        LocalDateTime date = LocalDateTime.of(2021, 05, 02, 16, 00, 00);

        Calendar calendar = Calendar.createCalender(title, content, date, shareType, user);
        return calendar;
    }

    public static Calendar getPublicCalendar(User user, long id) {
        Calendar calendar = getPublicCalendar(user);
        ReflectionTestUtils.setField(calendar, "id", id);
        return calendar;
    }

}
