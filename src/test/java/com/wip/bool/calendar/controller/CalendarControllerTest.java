package com.wip.bool.calendar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.calendar.dto.CalendarDto;
import com.wip.bool.calendar.repository.Calendar;
import com.wip.bool.calendar.repository.CalendarRepository;
import com.wip.bool.calendar.service.CalendarService;
import com.wip.bool.cmmn.type.ShareType;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.user.domain.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CalendarControllerTest {

    @InjectMocks
    private CalendarController calendarController;

    @Mock
    private CalendarService calendarService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CalendarRepository calendarRepository;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(calendarController).build();
        objectMapper = new ObjectMapper();
    }

    private Optional<User> getUser() {

        String email = "test@gmail.com";
        String name = "";
        String profile = "";
        UserType userType = UserType.WIP;
        Role role = Role.ROLE_NORMAL;
        User user = User.createUser(email, name, profile, userType, role);
        ReflectionTestUtils.setField(user, "id", 1L);
        return Optional.ofNullable(user);
    }

    private Calendar getCalendar(User user) {

        String title = "일정 추가!";
        String content = "네트워크 일정";
        ShareType shareType = ShareType.PUBLIC;
        LocalDateTime date = LocalDateTime.of(2021, 05, 02, 16, 00, 00);

        Calendar calendar = Calendar.createCalender(title, content, date, shareType, user);
        ReflectionTestUtils.setField(calendar, "id", 1L);
        return calendar;
    }

    private List<CalendarDto.CalendarResponse> getDeptCalendars() {

        List<CalendarDto.CalendarResponse> calendars = Arrays.asList(
                new CalendarDto.CalendarResponse(1L, "OO부서 회의", "모니터링 시스템 검토", LocalDateTime.of(2021, 05, 01, 14, 00, 00),
                        ShareType.DEPT, "test@gmail.com", 1L),
                new CalendarDto.CalendarResponse(2L, "OO부서 회의 - 1", "모니터링 시스템 검토", LocalDateTime.of(2021, 05, 02, 14, 00, 00),
                        ShareType.PUBLIC, "test2@gmail.com", 1L),
                new CalendarDto.CalendarResponse(3L, "OO부서 회의 - 2", "모니터링 시스템 검토", LocalDateTime.of(2021, 05, 03, 14, 00, 00),
                        ShareType.DEPT, "test2@gmail.com", 1L)
        );

        return calendars;
    }

    private List<CalendarDto.CalendarResponse> getIndividualCalendars() {

        List<CalendarDto.CalendarResponse> calendars = Arrays.asList(
                new CalendarDto.CalendarResponse(1L, "OO부서 회의", "모니터링 시스템 검토", LocalDateTime.of(2021, 05, 01, 14, 00, 00),
                        ShareType.PRIVATE, "test@gmail.com", 1L),
                new CalendarDto.CalendarResponse(2L, "OO부서 회의 - 1", "모니터링 시스템 검토", LocalDateTime.of(2021, 05, 02, 14, 00, 00),
                        ShareType.PRIVATE, "test2@gmail.com", 1L),
                new CalendarDto.CalendarResponse(3L, "OO부서 회의 - 2", "모니터링 시스템 검토", LocalDateTime.of(2021, 05, 03, 14, 00, 00),
                        ShareType.PRIVATE, "test2@gmail.com", 1L)
        );

        return calendars;
    }

    @Test
    public void 일정_추가_Controller() throws Exception {

        //given
        String title = "일정 추가!";
        String content = "네트워크 일정";
        ShareType shareType = ShareType.PUBLIC;
        LocalDateTime date = LocalDateTime.of(2021, 05, 02, 16, 00, 00);

        CalendarDto.CalendarSaveRequest requestDto = new CalendarDto.CalendarSaveRequest();
        ReflectionTestUtils.setField(requestDto, "title", title);
        ReflectionTestUtils.setField(requestDto, "content", content);
        ReflectionTestUtils.setField(requestDto, "shareType", shareType.name());
        ReflectionTestUtils.setField(requestDto, "calendarDate", Timestamp.valueOf(date).getTime());

        Optional<User> opt = getUser();
        Calendar calendar = getCalendar(opt.get());

        doReturn(calendar.getId()).when(calendarService).save(any(Long.class), any(CalendarDto.CalendarSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/calendar")
        .header("userId", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        final Long id = Long.valueOf(mvcResult.getResponse().getContentAsString());
        assertThat(id).isGreaterThan(0L);
    }

    @Test
    public void 일정_리스트_가져오기_부서_Controller() throws Exception {

        //given
        List<CalendarDto.CalendarResponse> deptCalendars = getDeptCalendars();

        doReturn(deptCalendars).when(calendarService).getDeptCalendars(any(Long.class), any(Long.class), any(Long.class));
        Long from = Timestamp.valueOf(LocalDateTime.of(2021, 05, 01, 00, 00, 00)).getTime();
        Long to = Timestamp.valueOf(LocalDateTime.of(2021, 05, 31, 23, 59, 59)).getTime();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("from", String.valueOf(from));
        params.add("to", String.valueOf(to));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/calendar-dept")
        .header("userId", 1L)
        .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        List<CalendarDto.CalendarResponse> values = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        assertThat(values.size()).isEqualTo(3);
    }

    @Test
    public void 일정_리스트_가져오기_개인_Controller() throws Exception {

        //given
        List<CalendarDto.CalendarResponse> individualCalendars = getIndividualCalendars();

        doReturn(individualCalendars).when(calendarService).getIndividualCalenders(any(Long.class), any(Long.class), any(Long.class));
        Long from = Timestamp.valueOf(LocalDateTime.of(2021, 05, 01, 00, 00, 00)).getTime();
        Long to = Timestamp.valueOf(LocalDateTime.of(2021, 05, 31, 23, 59, 59)).getTime();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("from", String.valueOf(from));
        params.add("to", String.valueOf(to));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/calendar-individual")
                .header("userId", 1L)
                .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        List<CalendarDto.CalendarResponse> values = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        assertThat(values.size()).isEqualTo(3);

    }

    @Test
    public void 일정_삭제_Controller() throws Exception {

        //given
        Optional<User> opt = getUser();
        Calendar calendar = getCalendar(opt.get());

        doReturn(1L).when(calendarService).delete(any(Long.class), any(Long.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/calendar/" + 1)
        .header("userId", 1L)
        .contentType(MediaType.APPLICATION_JSON));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long resValue = Long.valueOf(mvcResult.getResponse().getContentAsString());

        assertThat(resValue).isEqualTo(1L);
    }
}
