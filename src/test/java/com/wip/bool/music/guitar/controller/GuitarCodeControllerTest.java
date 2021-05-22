package com.wip.bool.music.guitar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.guitar.dto.GuitarCodeDto;
import com.wip.bool.music.guitar.service.GuitarCodeService;
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
import static org.mockito.Mockito.doReturn;
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

    private GuitarCode getGuitarCode() {
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode();
        ReflectionTestUtils.setField(guitarCode, "id", 1L);
        return guitarCode;
    }

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(guitarCodeController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("기타코드 추가")
    @Test
    public void 기타코드_추가_Controller() throws Exception {

        //given
        GuitarCodeDto.GuitarCodeSaveRequest requestDto = new GuitarCodeDto.GuitarCodeSaveRequest();
        ReflectionTestUtils.setField(requestDto, "code", "B");

        doReturn(1L).when(guitarCodeService).saveGuitarCode(any(GuitarCodeDto.GuitarCodeSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/music/guitar/code")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(id).isEqualTo(1L);
    }

    @DisplayName("기타코드 삭제")
    @Test
    public void 기타코드_삭제_Controller() throws Exception {

        //given
        doReturn(1L).when(guitarCodeService).deleteGuitarCode(any(Long.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/music/guitar/code/1"));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long resValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);

        assertThat(resValue).isEqualTo(1L);
    }

    @DisplayName("기타코드 리스트 조회")
    @Test
    public void 기타코드_리스트_조회_Controller() throws Exception {

        //given
        List<GuitarCodeDto.GuitarCodeResponse> guitarCodeResponses = new ArrayList<>();
        guitarCodeResponses.add(new GuitarCodeDto.GuitarCodeResponse(getGuitarCode()));
        doReturn(guitarCodeResponses).when(guitarCodeService).getGuitarCodes();

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/music/guitar/codes"));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                .andExpect(status().isOk())
                                                .andExpect(jsonPath("$").isArray())
                                                .andExpect(jsonPath("$[0]['guitar_code_id']").value(1L))
                                                .andReturn();

    }
}
