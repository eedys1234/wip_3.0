package com.wip.bool.recent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.cmmn.bible.WordsMasterFactory;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.music.song.SongDetailFactory;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.cmmn.recent.RecentFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.recent.domain.Recent;
import com.wip.bool.recent.dto.RecentDto;
import com.wip.bool.recent.service.RecentService;
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
public class RecentControllerTest {

    @InjectMocks
    private RecentController recentController;

    @Mock
    private RecentService recentService;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() throws Exception {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(recentController).build();
    }

    @DisplayName("최근들은내역 추가")
    @Test
    public void 최근들은내역_추가_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);

        Recent recent = RecentFactory.getRecent(user, songDetail, 1L);

        RecentDto.RecentSaveRequest requestDto = new RecentDto.RecentSaveRequest();
        ReflectionTestUtils.setField(requestDto, "songDetailId", songDetail.getId());

        doReturn(recent.getId()).when(recentService).saveRecent(anyLong(), any(RecentDto.RecentSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recent")
                                                                                    .header("userId", user.getId())
                                                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                    .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        Long id =  objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(id).isEqualTo(recent.getId());

        //verify
        verify(recentService, times(1)).saveRecent(anyLong(), any(RecentDto.RecentSaveRequest.class));

    }

    @DisplayName("최근들은내역 조회")
    @Test
    public void 최근들은내역_조회_Controller() throws Exception {

        //given
        int size = 10;
        int offset = 0;
        User user = UserFactory.getNormalUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);

        List<SongDetail> songDetails = SongDetailFactory.getSongDetailsWithId(songMaster, guitarCode, wordsMaster);
        List<Recent> recents = RecentFactory.getRcents(user, songDetails);

        doReturn(recents.stream()
        .map(recent -> new RecentDto.RecentResponse(recent.getId(), recent.getSongDetail().getId(), recent.getSongDetail().getTitle(), recent.getCreateDate()))
        .collect(Collectors.toList())).when(recentService).findAll(anyLong(), anyInt(), anyInt());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("size", String.valueOf(size));
        params.add("offset", String.valueOf(offset));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recents")
                                                                                    .header("userId", user.getId())
                                                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                    .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]['song_detail_id']").value(recents.get(0).getSongDetail().getId()))
                .andExpect(jsonPath("$[0]['title']").value(recents.get(0).getSongDetail().getTitle()))
                .andReturn();

        //verify
        verify(recentService, times(1)).findAll(anyLong(), anyInt(), anyInt());
    }
}
