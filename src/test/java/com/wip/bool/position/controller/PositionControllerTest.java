package com.wip.bool.position.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.cmmn.position.PositionFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.position.domain.Position;
import com.wip.bool.position.dto.PositionDto;
import com.wip.bool.position.service.PositionService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PositionControllerTest {

    @InjectMocks
    private PositionController positionController;

    @Mock
    private PositionService positionService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(positionController).build();
        objectMapper = new ObjectMapper();
    }

    private User getAdminUser() {
        User user = UserFactory.getAdminUser();
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private Position getPosition() {
        Position position = PositionFactory.getPosition();
        ReflectionTestUtils.setField(position, "id", 1L);
        return position;
    }

    private Position getPosition(String positionName, long id) {
        Position position = PositionFactory.getPosition(positionName);
        ReflectionTestUtils.setField(position, "id", id);
        return position;
    }

    @DisplayName("직위 추가")
    @Test
    public void 직위_추가_Controller() throws Exception {

        //given
        User user = getAdminUser();
        Position position = getPosition();
        PositionDto.PositionSaveRequest requestDto = new PositionDto.PositionSaveRequest();
        ReflectionTestUtils.setField(requestDto, "positionName", position.getPositionName());

        doReturn(position.getId()).when(positionService).savePosition(any(Long.class), any(PositionDto.PositionSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/position")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(id).isGreaterThan(0L);

        //verify
        verify(positionService, times(1)).savePosition(any(Long.class), any(PositionDto.PositionSaveRequest.class));
    }

    @DisplayName("직위 수정")
    @Test
    public void 직위_수정_Controller() throws Exception {

        //given
        User user = getAdminUser();
        Position position = getPosition();
        PositionDto.PositionUpdateRequest requestDto = new PositionDto.PositionUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "positionName", position.getPositionName());

        doReturn(position.getId()).when(positionService).updatePosition(any(Long.class), any(Long.class), any(PositionDto.PositionUpdateRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/position/1")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(id).isGreaterThan(0L);
        assertThat(id).isEqualTo(position.getId());

        //verify
        verify(positionService, times(1)).updatePosition(any(Long.class), any(Long.class), any(PositionDto.PositionUpdateRequest.class));
    }

    @DisplayName("직위 삭제")
    @Test
    public void 직위_삭제_Controller() throws Exception {

        //given
        User user = getAdminUser();
        doReturn(1L).when(positionService).deletePosition(any(Long.class), any(Long.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/position/1")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long resValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(positionService, times(1)).deletePosition(any(Long.class), any(Long.class));

    }

    @DisplayName("직위 리스트 조회")
    @Test
    public void 직위_리스트_조회_Controller() throws Exception {

        //given
        String[] positionNames = {"리더", "부장", "차장", "대리", "사원"};
        List<Position> positions = new ArrayList<>();

        for(int i=0;i<positionNames.length;i++) {
            Position position = getPosition(positionNames[i], i+1);
            positions.add(position);
        }

        doReturn(positions.stream()
                .map(PositionDto.PositionResponse::new)
                .collect(Collectors.toList())).when(positionService).findAll();

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/positions"));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                .andExpect(status().isOk())
                                                .andExpect(jsonPath("$").isArray())
                                                .andExpect(jsonPath("$[0]['position_name']").value("리더"))
                                                .andReturn();

        //verify
        verify(positionService, times(1)).findAll();
    }

}
