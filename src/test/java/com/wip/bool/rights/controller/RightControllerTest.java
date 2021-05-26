package com.wip.bool.rights.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.cmmn.rights.RightsFactory;
import com.wip.bool.rights.domain.Rights;
import com.wip.bool.rights.dto.RightDto;
import com.wip.bool.rights.service.RightService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RightControllerTest {

    @InjectMocks
    private RightController rightController;

    @Mock
    private RightService rightService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Rights getRight() {
        Rights rights = RightsFactory.getRights();
        ReflectionTestUtils.setField(rights, "id", 1L);
        return rights;
    }

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(rightController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("권한 추가")
    @Test
    public void 권한_추가_Controller() throws Exception {

        //given
        Rights right = getRight();
        RightDto.RightSaveRequest requestDto = new RightDto.RightSaveRequest();
        ReflectionTestUtils.setField(requestDto, "target", "USERBOX");
        ReflectionTestUtils.setField(requestDto, "targetId", 1L);
        ReflectionTestUtils.setField(requestDto, "authority", "GROUP");
        ReflectionTestUtils.setField(requestDto, "authorityId", 1L);
        ReflectionTestUtils.setField(requestDto, "rightType", String.join(Rights.RightType.WRITE.name(), Rights.RightType.READ.name()));

        doReturn(right.getId()).when(rightService).saveRight(any(RightDto.RightSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/right")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();
        assertThat(id).isEqualTo(right.getId());

        //verify
        verify(rightService, times(1)).saveRight(any(RightDto.RightSaveRequest.class));

    }

    @DisplayName("권한 삭제")
    @Test
    public void 권한_삭제_Controller() throws Exception {

        //given
        doReturn(1L).when(rightService).deleteRight(anyLong(), anyString());

        MultiValueMap params = new LinkedMultiValueMap();
        params.add("right_type", Rights.RightType.READ.name());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/right/1")
        .params(params)
        .contentType(MediaType.APPLICATION_JSON));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long resValue = response.getResult();
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(rightService, times(1)).deleteRight(anyLong(), anyString());
    }
}
