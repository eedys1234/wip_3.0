package com.wip.bool.bible.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.bible.dto.WordsMasterDto;
import com.wip.bool.bible.service.WordsMasterService;
import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.cmmn.bible.WordsMasterFactory;
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

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class WordsMasterControllerTest {

    @InjectMocks
    private WordsMasterController wordsMasterController;

    @Mock
    private WordsMasterService wordsMasterService;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    
    @BeforeEach
    public void init() throws Exception {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(wordsMasterController).build();
    }

    @DisplayName("단원 추가")
    @Test
    public void 단원_추가_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        doReturn(wordsMaster.getId()).when(wordsMasterService).saveWordsMaster(anyLong(), any(WordsMasterDto.WordsMasterSaveRequest.class));
        WordsMasterDto.WordsMasterSaveRequest requestDto = new WordsMasterDto.WordsMasterSaveRequest();
        ReflectionTestUtils.setField(requestDto, "wordsName", wordsMaster.getWordsName());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/words-master")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();
        assertThat(id).isEqualTo(wordsMaster.getId());

        //verify
        verify(wordsMasterService, times(1)).saveWordsMaster(anyLong(), any(WordsMasterDto.WordsMasterSaveRequest.class));
    }

    @DisplayName("단원 삭제")
    @Test
    public void 단원_삭제_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        doReturn(1L).when(wordsMasterService).deleteWordsMaster(anyLong(), anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/words-master/1")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long resValue = response.getResult();
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(wordsMasterService, times(1)).deleteWordsMaster(anyLong(), anyLong());
    }

    @DisplayName("단원 리스트 조회")
    @Test
    public void 단원_리스트_조회_Controller() throws Exception {

        //given
        List<WordsMaster> wordsMasters = WordsMasterFactory.getWordsMastersWithId();

        doReturn(wordsMasters.stream()
        .map(WordsMasterDto.WordsMasterResponse::new)
        .collect(Collectors.toList())).when(wordsMasterService).findAll();

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/words-masters")
        .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                .andExpect(status().isOk())
                                                .andExpect(jsonPath("$.result").isArray())
                                                .andExpect(jsonPath("$.result[0]['words_name']").value(wordsMasters.get(0).getWordsName()))
                                                .andExpect(jsonPath("$.result[0]['words_master_id']").value(wordsMasters.get(0).getId()))
                                                .andReturn();

        ApiResponse<List<WordsMasterDto.WordsMasterResponse>> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<ApiResponse<List<WordsMasterDto.WordsMasterResponse>>>() {});

        List<WordsMasterDto.WordsMasterResponse> values = response.getResult();
        assertThat(values.size()).isEqualTo(wordsMasters.size());

        //verify
        verify(wordsMasterService, times(1)).findAll();
    }
}
