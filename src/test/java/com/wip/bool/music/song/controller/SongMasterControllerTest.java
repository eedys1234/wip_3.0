package com.wip.bool.music.song.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.music.song.dto.SongMasterDto;
import com.wip.bool.music.song.service.SongMasterService;
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
public class SongMasterControllerTest {

    @InjectMocks
    private SongMasterController songMasterController;

    @Mock
    private SongMasterService songMasterService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(songMasterController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("SongMaster 추가")
    @Test
    public void SongMaster_추가_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        SongMasterDto.SongMasterSaveRequest requestDto = new SongMasterDto.SongMasterSaveRequest();
        ReflectionTestUtils.setField(requestDto, "codeName", SongMasterFactory.getCodeNames().get(0));
        doReturn(songMaster.getId()).when(songMasterService).saveSongMaster(any(Long.class), any(SongMasterDto.SongMasterSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/music/song-master")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(id).isEqualTo(songMaster.getId());

        //verify
        verify(songMasterService, times(1)).saveSongMaster(any(Long.class), any(SongMasterDto.SongMasterSaveRequest.class));
    }

    @DisplayName("SongMaster 삭제")
    @Test
    public void SongMaster_삭제_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        doReturn(1L).when(songMasterService).deleteSongMaster(any(Long.class), any(Long.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/music/song-master/1")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long resValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(songMasterService, times(1)).deleteSongMaster(any(Long.class), any(Long.class));
    }

    @DisplayName("SongMaster 리스트 조회")
    @Test
    public void SongMaster_리스트_조회_Controller() throws Exception {

        //given
        List<SongMaster> songMasters = SongMasterFactory.getSongMastersWithId();
        doReturn(songMasters.stream()
                            .map(SongMasterDto.SongMasterResponse::new)
                            .collect(Collectors.toList())).when(songMasterService).getsSongMaster();

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/music/song-masters")
        .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]['code_name']").value(songMasters.get(0).getCodeName()))
                .andReturn();

        //verify
        verify(songMasterService, times(1)).getsSongMaster();
    }
}
