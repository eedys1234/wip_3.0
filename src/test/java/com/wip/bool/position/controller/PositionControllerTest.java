package com.wip.bool.position.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.cmmn.ApiResponse;
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

    @DisplayName("직위 추가")
    @Test
    public void 직위_추가_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        Position position = PositionFactory.getPosition(1L);
        PositionDto.PositionSaveRequest requestDto = new PositionDto.PositionSaveRequest();
        ReflectionTestUtils.setField(requestDto, "positionName", position.getPositionName());

        doReturn(position.getId()).when(positionService).savePosition(anyLong(), any(PositionDto.PositionSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/position")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();
        assertThat(id).isGreaterThan(0L);
        assertThat(id).isEqualTo(position.getId());

        //verify
        verify(positionService, times(1)).savePosition(anyLong(), any(PositionDto.PositionSaveRequest.class));
    }

    @DisplayName("직위 수정")
    @Test
    public void 직위_수정_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        Position position = PositionFactory.getPosition(1L);
        PositionDto.PositionUpdateRequest requestDto = new PositionDto.PositionUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "positionName", position.getPositionName());

        doReturn(position.getId()).when(positionService).updatePosition(anyLong(), anyLong(), any(PositionDto.PositionUpdateRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/position/1")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();
        assertThat(id).isGreaterThan(0L);
        assertThat(id).isEqualTo(position.getId());

        //verify
        verify(positionService, times(1)).updatePosition(anyLong(), anyLong(), any(PositionDto.PositionUpdateRequest.class));
    }

    @DisplayName("직위 삭제")
    @Test
    public void 직위_삭제_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        doReturn(1L).when(positionService).deletePosition(anyLong(), anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/position/1")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long resValue = response.getResult();
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(positionService, times(1)).deletePosition(anyLong(), anyLong());

    }

    @DisplayName("직위 리스트 조회")
    @Test
    public void 직위_리스트_조회_Controller() throws Exception {

        //given
        List<Position> positions = PositionFactory.getPositionsWithId();

        doReturn(positions.stream()
                .map(PositionDto.PositionResponse::new)
                .collect(Collectors.toList())).when(positionService).findAll();

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/positions"));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                .andExpect(status().isOk())
                                                .andExpect(jsonPath("$.result").isArray())
                                                .andExpect(jsonPath("$.result[0]['position_name']").value("리더"))
                                                .andReturn();

        //verify
        verify(positionService, times(1)).findAll();
    }

}
