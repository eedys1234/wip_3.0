package com.wip.bool.calendar.service;

import com.wip.bool.calendar.dto.CalendarDto;
import com.wip.bool.calendar.repository.Calendar;
import com.wip.bool.calendar.repository.CalendarRepository;
import com.wip.bool.cmmn.calendar.CalendarFactory;
import com.wip.bool.cmmn.dept.DeptFactory;
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


    @DisplayName("일정 추가")
    @Test
    public void 일정_추가_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        Calendar calendar = CalendarFactory.getPublicCalendar(user, 1L);
        CalendarDto.CalendarSaveRequest requestDto = getCalendarSaveRequestDto();

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(calendar).when(calendarRepository).save(any(Calendar.class));

        Long id = calendarService.save(user.getId(), requestDto);

        //then
        assertThat(id).isGreaterThan(0L);
        assertThat(id).isEqualTo(calendar.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(calendarRepository, times(1)).save(any(Calendar.class));

    }

    @DisplayName("일정 리스트 가져오기 부서")
    @Test
    public void 일정_리스트_가져오기_부서_Service() throws Exception {

        //given
        Dept dept = DeptFactory.getDept(1L);
        User user = UserFactory.getNormalUser(dept, 1L);

        List<Long> userIds = Arrays.asList(1L, 2L, 3L);

        List<CalendarDto.CalendarResponse> deptCalendars = CalendarFactory.getDeptCalendars();

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).deptByUser(anyLong());
        doReturn(userIds).when(userRepository).usersByDept(anyLong());
        doReturn(deptCalendars).when(calendarRepository).deptCalendars(any(List.class), any(LocalDateTime.class), any(LocalDateTime.class));

        LocalDateTime fromDate = LocalDateTime.of(2021, 05, 01, 00, 00, 00);
        LocalDateTime toDate = LocalDateTime.of(2021, 05, 31, 23, 59, 59);

        List<CalendarDto.CalendarResponse> values = calendarService.getDeptCalendars(1L, Timestamp.valueOf(fromDate).getTime(), Timestamp.valueOf(toDate).getTime());

        //then
        assertThat(values.size()).isEqualTo(deptCalendars.size());
        assertThat(values).extracting(CalendarDto.CalendarResponse::getCalendarId)
                .containsAll(deptCalendars.stream().map(CalendarDto.CalendarResponse::getCalendarId).collect(Collectors.toList()));

        assertThat(values).extracting(CalendarDto.CalendarResponse::getUserId)
                .allMatch(u -> u == user.getId());

        assertThat(values).extracting(CalendarDto.CalendarResponse::getShareType)
                .noneMatch(share -> share.equals(ShareType.PRIVATE));

        assertThat(values).extracting(v -> Timestamp.valueOf(v.getCalendarDate()).getTime())
                .containsAll(deptCalendars.stream().map(c -> Timestamp.valueOf(c.getCalendarDate()).getTime()).collect(Collectors.toList()));

        //verify
        verify(userRepository, times(1)).deptByUser(anyLong());
        verify(userRepository, times(1)).usersByDept(anyLong());
        verify(calendarRepository, times(1)).deptCalendars(any(List.class), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @DisplayName("일정 리스트 가져오기 개인")
    @Test
    public void 일정_리스트_가져오기_개인_Service() throws Exception {

        //given
        Dept dept = DeptFactory.getDept(1L);
        User user = UserFactory.getNormalUser(dept, 1L);

        List<CalendarDto.CalendarResponse> individualCalendars = CalendarFactory.getIndividualCalendars();

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(individualCalendars).when(calendarRepository).individualCalendars(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));

        LocalDateTime fromDate = LocalDateTime.of(2021, 05, 01, 00, 00, 00);
        LocalDateTime toDate = LocalDateTime.of(2021, 05, 31, 23, 59, 59);

        List<CalendarDto.CalendarResponse> values = calendarService.getIndividualCalenders(1L, Timestamp.valueOf(fromDate).getTime(), Timestamp.valueOf(toDate).getTime());

        //then
        assertThat(values.size()).isEqualTo(individualCalendars.size());
        assertThat(values).extracting(CalendarDto.CalendarResponse::getShareType)
                .allMatch(shareType -> shareType.equals(ShareType.PRIVATE));

        assertThat(values).extracting(CalendarDto.CalendarResponse::getCalendarId)
                .containsAll(individualCalendars.stream().map(calendar -> calendar.getCalendarId()).collect(Collectors.toList()));

        assertThat(values).extracting(CalendarDto.CalendarResponse::getUserId)
                .allMatch(u -> u == user.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(calendarRepository, times(1)).individualCalendars(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @DisplayName("일정 삭제")
    @Test
    public void 일정_삭제_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        Calendar calendar = CalendarFactory.getPublicCalendar(user, 1L);

        //when
        doReturn(Optional.ofNullable(calendar)).when(calendarRepository).findByIdAndUserId(anyLong(), anyLong());
        doReturn(1L).when(calendarRepository).delete(any(Calendar.class));
        Long resValue = calendarService.delete(user.getId(), calendar.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(calendarRepository, times(1)).findByIdAndUserId(anyLong(), anyLong());
        verify(calendarRepository, times(1)).delete(any(Calendar.class));
    }
}
