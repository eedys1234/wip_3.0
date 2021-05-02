package com.wip.bool.calendar.repository;

import com.wip.bool.calendar.dto.CalendarDto;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.dept.domain.Dept;
import com.wip.bool.dept.domain.DeptRepository;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.user.domain.UserType;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(TestConfig.class)
@SpringBootTest
@ActiveProfiles(value = "test")
@Transactional
public class CalendarRepositoryTest {

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private DeptRepository deptRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void 사용자추가() throws Exception {

        //Dept 추가
        String deptName = "밍공";
        Dept dept = Dept.builder().deptName(deptName).build();
        deptRepository.save(dept);

        String email = "test@gmail.com";
        String name = "test";
        String password = "1234!@#$";
        String profile = "";

        User userA = User.createUser(email, name, password, profile, UserType.WIP, Role.ROLE_NORMAL);
        User userB = User.createUser(email+"B", name+"B", password, profile, UserType.WIP, Role.ROLE_NORMAL);

        userA.updateDept(dept);
        userB.updateDept(dept);

        userRepository.save(userA);
        userRepository.save(userB);
    }

    @Test
    public void 일정_저장() throws Exception {

        //given
        String title = "본사 출근";
        String content = "OO팀 부서와의 OO관련 회의";
        ShareType shareType = ShareType.DEPT;
        LocalDateTime now = LocalDateTime.now();

        List<User> users = userRepository.findAll();

        Calendar calendar = Calendar.createCalender(title, content, now, shareType, users.get(0));

        //when
        Calendar addCalendar = calendarRepository.save(calendar);

        //then
        assertThat(addCalendar.getId()).isGreaterThan(0L);
        assertThat(addCalendar.getTitle()).isEqualTo(title);
        assertThat(addCalendar.getContent()).isEqualTo(content);
        assertThat(addCalendar.getCalendarDate()).isBefore(LocalDateTime.now());
        assertThat(addCalendar.getShareType()).isEqualTo(ShareType.DEPT);
    }

    @DisplayName("사용자가 속한 부서원들과 전체의 일정 가져오기")
    @Test
    public void 일정_리스트_가져오기_부서() throws Exception {

        //given
        List<User> users = userRepository.findAll();
        String title = "본사 출근";
        String content = "OO팀 부서와의 OO관련 회의";
        ShareType shareType = ShareType.DEPT;
        LocalDateTime now = LocalDateTime.now();

        for(int i=0;i<10;i++) {
            Calendar calendar = Calendar.createCalender(title + i, content, now, shareType, users.get(1));
            calendarRepository.save(calendar);
        }

        Calendar calendar = Calendar.createCalender(title, content, now, ShareType.PUBLIC, users.get(0));
        calendarRepository.save(calendar);

        LocalDateTime fromDate = LocalDateTime.of(2021, 05, 01, 00, 00, 00);
        LocalDateTime toDate = LocalDateTime.of(2021, 05, 31, 00, 00, 00);

        List<Long> userIds = userRepository.usersByDept(users.get(0).getDept().getId());

        //when
        List<CalendarDto.CalendarResponse> deptCalendars = calendarRepository.deptCalendars(userIds, fromDate, toDate);

        //then
        assertThat(deptCalendars.size()).isEqualTo(11);
        assertThat(deptCalendars).extracting(CalendarDto.CalendarResponse::getShareType)
                .noneMatch(a -> a.equals(ShareType.PRIVATE));

        assertThat(deptCalendars).extracting(CalendarDto.CalendarResponse::getCalendarDate)
                .allMatch(a -> a.isBefore(LocalDateTime.now()));

        assertThat(deptCalendars).extracting(CalendarDto.CalendarResponse::getUserId).isNotNull();
        assertThat(deptCalendars).extracting(CalendarDto.CalendarResponse::getTitle).isNotNull();
        assertThat(deptCalendars).extracting(CalendarDto.CalendarResponse::getContent).isNotNull();

    }

    @DisplayName(value = "사용자가 등록한 개인일정 리스트 가져오기")
    @Test
    public void 일정_가져오기_개인() throws Exception {

        //given
        String title = "본사 출근";
        String content = "OO팀 부서와의 OO관련 회의";
        ShareType shareType = ShareType.PUBLIC;
        LocalDateTime now = LocalDateTime.now();

        List<User> users = userRepository.findAll();

        Calendar calendar = Calendar.createCalender(title, content, now, shareType, users.get(0));
        Calendar addCalendar = calendarRepository.save(calendar);

        LocalDateTime fromDate = LocalDateTime.of(2021, 05, 01, 00, 00, 00);
        LocalDateTime toDate = LocalDateTime.of(2021, 05, 31, 00, 00, 00);

        //when
        List<CalendarDto.CalendarResponse> calendars = calendarRepository.individualCalendars(users.get(0).getId(), fromDate, toDate);

        //then
        assertThat(calendars.get(0).getTitle()).isEqualTo(title);
        assertThat(calendars.get(0).getContent()).isEqualTo(content);
        assertThat(calendars.size()).isEqualTo(1);
        assertThat(calendars.get(0).getCalendarDate()).isBefore(LocalDateTime.now());
        assertThat(calendars.get(0).getCalendarId()).isGreaterThan(0L);
    }

}
