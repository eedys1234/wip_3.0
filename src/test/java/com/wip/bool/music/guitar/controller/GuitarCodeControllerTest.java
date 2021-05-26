package com.wip.bool.music.guitar.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.music.guitar.dto.GuitarCodeDto;
import com.wip.bool.music.guitar.service.GuitarCodeService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class GuitarCodeControllerTest {

    @InjectMocks
    private GuitarCodeController guitarCodeController;

    @Mock
    private GuitarCodeService guitarCodeService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;


    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(guitarCodeController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("기타코드 추가")
    @Test
    public void 기타코드_추가_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        GuitarCodeDto.GuitarCodeSaveRequest requestDto = new GuitarCodeDto.GuitarCodeSaveRequest();
        ReflectionTestUtils.setField(requestDto, "code", "B");

        doReturn(1L).when(guitarCodeService).saveGuitarCode(anyLong(), any(GuitarCodeDto.GuitarCodeSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/music/guitar/code")
                                                    .header("userId", user.getId())
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();
        assertThat(id).isEqualTo(1L);

        //verify
        verify(guitarCodeService, times(1)).saveGuitarCode(anyLong(), any(GuitarCodeDto.GuitarCodeSaveRequest.class));
    }

    @DisplayName("기타코드 삭제")
    @Test
    public void 기타코드_삭제_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        doReturn(1L).when(guitarCodeService).deleteGuitarCode(anyLong(), anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/music/guitar/code/1")
                                                                                    .header("userId", user.getId())
                                                                                    .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long resValue = response.getResult();
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(guitarCodeService, times(1)).deleteGuitarCode(anyLong(), anyLong());
    }

    @DisplayName("기타코드 리스트 조회")
    @Test
    public void 기타코드_리스트_조회_Controller() throws Exception {

        //given
        List<GuitarCodeDto.GuitarCodeResponse> guitarCodeResponses = new ArrayList<>();
        guitarCodeResponses.add(new GuitarCodeDto.GuitarCodeResponse(GuitarCodeFactory.getGuitarCode(1L)));
        doReturn(guitarCodeResponses).when(guitarCodeService).getGuitarCodes();

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/music/guitar/codes"));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                .andExpect(status().isOk())
                                                .andExpect(jsonPath("$.result").isArray())
                                                .andExpect(jsonPath("$.result.[0]['guitar_code_id']").value(1L))
                                                .andReturn();

        //verify
        verify(guitarCodeService, times(1)).getGuitarCodes();
    }
}
