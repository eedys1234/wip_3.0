package com.wip.bool.calendar.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.calendar.dto.CalendarDto;
import com.wip.bool.calendar.repository.Calendar;
import com.wip.bool.calendar.service.CalendarService;
import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.cmmn.calendar.CalendarFactory;
import com.wip.bool.cmmn.type.ShareType;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CalendarControllerTest {

    @InjectMocks
    private CalendarController calendarController;

    @Mock
    private CalendarService calendarService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(calendarController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("?????? ??????")
    @Test
    public void ??????_??????_Controller() throws Exception {

        //given
        String title = "?????? ??????!";
        String content = "???????????? ??????";
        ShareType shareType = ShareType.PUBLIC;
        LocalDateTime date = LocalDateTime.of(2021, 05, 02, 16, 00, 00);

        CalendarDto.CalendarSaveRequest requestDto = new CalendarDto.CalendarSaveRequest();
        ReflectionTestUtils.setField(requestDto, "title", title);
        ReflectionTestUtils.setField(requestDto, "content", content);
        ReflectionTestUtils.setField(requestDto, "shareType", shareType.name());
        ReflectionTestUtils.setField(requestDto, "calendarDate", Timestamp.valueOf(date).getTime());

        User user = UserFactory.getNormalUser(1L);
        Calendar calendar = CalendarFactory.getPublicCalendar(user, 1L);

        doReturn(calendar.getId()).when(calendarService).saveCalendar(anyLong(), any(CalendarDto.CalendarSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/calendar")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        final ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        final Long id = response.getResult();

        assertThat(id).isGreaterThan(0L);
        assertThat(id).isEqualTo(calendar.getId());

        //verify
        verify(calendarService, times(1)).saveCalendar(anyLong(), any(CalendarDto.CalendarSaveRequest.class));
    }

    @DisplayName("?????? ????????? ???????????? ??????")
    @Test
    public void ??????_?????????_????????????_??????_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        List<Calendar> deptCalendars = CalendarFactory.getDeptCalendars(user);

        doReturn(deptCalendars.stream()
                .map(CalendarDto.CalendarResponse::new)
                .collect(Collectors.toList())).when(calendarService).getDeptCalendars(anyLong(), anyLong(), anyLong());

        LocalDateTime now = LocalDateTime.now();
        long from = Timestamp.valueOf(now.minusMonths(1)).getTime();
        long to = Timestamp.valueOf(now.plusMonths(1)).getTime();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("from", String.valueOf(from));
        params.add("to", String.valueOf(to));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/calendar-dept")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<List<CalendarDto.CalendarResponse>> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<List<CalendarDto.CalendarResponse>>>() {});
        List<CalendarDto.CalendarResponse> values = response.getResult();

        assertThat(values.size()).isEqualTo(deptCalendars.size());

        //verify
        verify(calendarService, times(1)).getDeptCalendars(anyLong(), anyLong(), anyLong());
    }

    @DisplayName("?????? ????????? ???????????? ??????")
    @Test
    public void ??????_?????????_????????????_??????_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        List<Calendar> individualCalendars = CalendarFactory.getIndividualCalendars(user);

        doReturn(individualCalendars.stream()
                .map(CalendarDto.CalendarResponse::new)
                .collect(Collectors.toList())).when(calendarService).getIndividualCalenders(anyLong(), anyLong(), anyLong());

        LocalDateTime now = LocalDateTime.now();

        Long from = Timestamp.valueOf(now.minusMonths(1)).getTime();
        Long to = Timestamp.valueOf(now.plusMonths(1)).getTime();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("from", String.valueOf(from));
        params.add("to", String.valueOf(to));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/calendar-individual")
                .header("userId", user.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<List<CalendarDto.CalendarResponse>> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<List<CalendarDto.CalendarResponse>>>() {});
        List<CalendarDto.CalendarResponse> values = response.getResult();
        assertThat(values.size()).isEqualTo(individualCalendars.size());

        //verify
        verify(calendarService, times(1)).getIndividualCalenders(anyLong(), anyLong(), anyLong());
    }

    @DisplayName("?????? ??????")
    @Test
    public void ??????_??????_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        Calendar calendar = CalendarFactory.getPublicCalendar(user, 1L);

        doReturn(1L).when(calendarService).deleteCalendar(anyLong(), anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/calendar/" + 1)
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long resValue = response.getResult();
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(calendarService, times(1)).deleteCalendar(anyLong(), anyLong());
    }
}
