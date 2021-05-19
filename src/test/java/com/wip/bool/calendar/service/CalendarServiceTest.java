package com.wip.bool.calendar.service;

import com.wip.bool.calendar.dto.CalendarDto;
import com.wip.bool.calendar.domain.Calendar;
import com.wip.bool.calendar.domain.CalendarRepository;
import com.wip.bool.cmmn.type.ShareType;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.dept.domain.Dept;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CalendarServiceTest {

    @InjectMocks
    private CalendarService calendarService;

    @Mock
    private CalendarRepository calendarRepository;

    @Mock
    private UserRepository userRepository;

    private User getUser() {
        User user = UserFactory.getNormalUser();
        ReflectionTestUtils.setField(user, "id", 1L);

        return user;
    }

    private Calendar getCalendar(User user) {
        String title = "본사 출근";
        String content = "OO팀 부서와의 OO관련 회의";
        ShareType shareType = ShareType.DEPT;
        LocalDateTime now = LocalDateTime.now();

        Calendar calendar = Calendar.createCalender(title, content, now, shareType, user);
        ReflectionTestUtils.setField(calendar, "id", 1L);
        return calendar;
    }

    private CalendarDto.CalendarSaveRequest getCalendarSaveRequestDto() {
        String title = "본사 출근";
        String content = "OO팀 부서와의 OO관련 회의";
        ShareType shareType = ShareType.DEPT;
        LocalDateTime now = LocalDateTime.now();

        CalendarDto.CalendarSaveRequest requestDto = new CalendarDto.CalendarSaveRequest();
        ReflectionTestUtils.setField(requestDto, "title", title);
        ReflectionTestUtils.setField(requestDto, "content", content);
        ReflectionTestUtils.setField(requestDto, "shareType", shareType.name());
        ReflectionTestUtils.setField(requestDto, "calendarDate", Timestamp.valueOf(now).getTime());

        return requestDto;
    }

    private List<Long> getUserIds() {

        List<Long> userIds = Arrays.asList(1L, 2L, 3L);
        return userIds;
    }

    private List<CalendarDto.CalendarResponse> getDeptCalendars() {

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

    private List<CalendarDto.CalendarResponse> getIndividualCalendars() {

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

    private Dept getDept() {
        Dept dept = new Dept("밍공");
        ReflectionTestUtils.setField(dept, "id", 1L);
        return dept;
    }

    @DisplayName("일정_추가하기")
    @Test
    public void 일정_추가_Service() throws Exception {

        //given
        Long userId = 1L;
        User user = getUser();
        Calendar calendar = getCalendar(user);
        CalendarDto.CalendarSaveRequest requestDto = getCalendarSaveRequestDto();

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(calendar)
                .when(calendarRepository)
                .save(any(Calendar.class));

        calendarService.save(userId, requestDto);

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(calendarRepository, times(1)).save(any(Calendar.class));

    }

    @Test
    public void 일정_리스트_가져오기_부서_Service() throws Exception {

        //given
        Dept dept = getDept();
        User user = getUser();
        user.updateDept(dept);
        List<Long> userIds = getUserIds();

        List<CalendarDto.CalendarResponse> deptCalendars = getDeptCalendars();

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).deptByUser(any(Long.class));
        doReturn(userIds).when(userRepository).usersByDept(any(Long.class));
        doReturn(deptCalendars).when(calendarRepository).deptCalendars(any(List.class), any(LocalDateTime.class), any(LocalDateTime.class));

        LocalDateTime fromDate = LocalDateTime.of(2021, 05, 01, 00, 00, 00);
        LocalDateTime toDate = LocalDateTime.of(2021, 05, 31, 23, 59, 59);

        List<CalendarDto.CalendarResponse> values = calendarService.getDeptCalendars(1L, Timestamp.valueOf(fromDate).getTime(), Timestamp.valueOf(toDate).getTime());

        //then
        assertThat(values.size()).isEqualTo(deptCalendars.size());
        assertThat(values).extracting(CalendarDto.CalendarResponse::getCalendarId)
                .containsExactly(1L, 2L, 3L);

        assertThat(values).extracting(CalendarDto.CalendarResponse::getUserId)
                .allMatch(u -> u == 1L);

        assertThat(values).extracting(CalendarDto.CalendarResponse::getShareType)
                .noneMatch(share -> share.equals(ShareType.PRIVATE));

        assertThat(values).extracting(v -> Timestamp.valueOf(v.getCalendarDate()).getTime())
                .containsAll(deptCalendars.stream().map(c -> Timestamp.valueOf(c.getCalendarDate()).getTime()).collect(Collectors.toList()));

        //verify
        verify(userRepository, times(1)).deptByUser(any(Long.class));
        verify(userRepository, times(1)).usersByDept(any(Long.class));
        verify(calendarRepository, times(1)).deptCalendars(any(List.class), any(LocalDateTime.class),
                any(LocalDateTime.class));
    }

    @Test
    public void 일정_리스트_가져오기_개인_Service() throws Exception {

        //given
        Dept dept = getDept();
        User user = getUser();
        user.updateDept(dept);
        List<CalendarDto.CalendarResponse> individualCalendars = getIndividualCalendars();

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(individualCalendars).when(calendarRepository).individualCalendars(any(Long.class), any(LocalDateTime.class), any(LocalDateTime.class));

        LocalDateTime fromDate = LocalDateTime.of(2021, 05, 01, 00, 00, 00);
        LocalDateTime toDate = LocalDateTime.of(2021, 05, 31, 23, 59, 59);

        List<CalendarDto.CalendarResponse> values = calendarService.getIndividualCalenders(1L, Timestamp.valueOf(fromDate).getTime(), Timestamp.valueOf(toDate).getTime());

        //then
        assertThat(values.size()).isEqualTo(3);
        assertThat(values).extracting(CalendarDto.CalendarResponse::getShareType)
                .allMatch(shareType -> shareType.equals(ShareType.PRIVATE));

        assertThat(values).extracting(CalendarDto.CalendarResponse::getCalendarId)
                .containsExactly(1L, 2L, 3L);

        assertThat(values).extracting(CalendarDto.CalendarResponse::getUserId)
                .allMatch(u -> u == 1L);

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(calendarRepository, times(1)).individualCalendars(any(Long.class),
                any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    public void 일정_삭제_Service() throws Exception {

        //given
        User user = getUser();
        Calendar calendar = getCalendar(user);

        //when
        doReturn(Optional.ofNullable(calendar)).when(calendarRepository).findByIdAndUserId(any(Long.class), any(Long.class));
        doReturn(1L).when(calendarRepository).delete(any(Calendar.class));
        Long resValue = calendarService.delete(user.getId(), calendar.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(calendarRepository, times(1)).findByIdAndUserId(any(Long.class), any(Long.class));
        verify(calendarRepository, times(1)).delete(any(Calendar.class));
    }
}
