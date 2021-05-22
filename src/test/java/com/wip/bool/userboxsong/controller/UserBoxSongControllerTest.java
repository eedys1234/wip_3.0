package com.wip.bool.userboxsong.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.cmmn.bible.WordsMasterFactory;
import com.wip.bool.cmmn.dept.DeptFactory;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.music.song.SongDetailFactory;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.cmmn.userbox.UserBoxFactory;
import com.wip.bool.cmmn.userboxsong.UserBoxSongFactory;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.user.domain.User;
import com.wip.bool.userbox.controller.UserBoxSongController;
import com.wip.bool.userbox.domain.UserBox;
import com.wip.bool.userbox.domain.UserBoxSong;
import com.wip.bool.userbox.dto.UserBoxSongDto;
import com.wip.bool.userbox.service.UserBoxSongService;
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

import java.util.ArrayList;
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
public class UserBoxSongControllerTest {

    @InjectMocks
    private UserBoxSongController userBoxSongController;

    @Mock
    private UserBoxSongService userBoxSongService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userBoxSongController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("사용자박스 곡 추가")
    @Test
    public void 사용자박스_곡_추가_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(DeptFactory.getDept(), 1L);
        UserBox userBox = UserBoxFactory.getUserBox(user, 1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);
        UserBoxSong userBoxSong = UserBoxSongFactory.getUserBoxSong(songDetail, userBox, 1L);

        doReturn(userBoxSong.getId()).when(userBoxSongService).saveUserBoxSong(anyLong(), anyLong(), any(UserBoxSongDto.UserBoxSongSaveRequest.class));

        UserBoxSongDto.UserBoxSongSaveRequest requestDto = new UserBoxSongDto.UserBoxSongSaveRequest();
        ReflectionTestUtils.setField(requestDto, "songDetailId", songDetail.getId());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/userbox/1/song")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(id).isEqualTo(userBoxSong.getId());

        //verify
        verify(userBoxSongService, times(1)).saveUserBoxSong(anyLong(), anyLong(), any(UserBoxSongDto.UserBoxSongSaveRequest.class));
    }

    @DisplayName("사용자박스 곡 삭제")
    @Test
    public void 사용자박스_곡_삭제_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(DeptFactory.getDept(), 1L);
        doReturn(1L).when(userBoxSongService).deleteUserBoxSong(anyLong(), anyLong(), anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/userbox/1/song/1")
                .header("userId", user.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long resValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userBoxSongService, times(1)).deleteUserBoxSong(anyLong(), anyLong(), anyLong());
    }

    @DisplayName("사용자박스 곡 리스트 조회")
    @Test
    public void 사용자박스_곡_리스트_조회_Controller() throws Exception {

        //given
        String order = "ASC";
        String sort = "TITLE";
        int size = 10;
        int offset = 0;

        User user = UserFactory.getNormalUser(DeptFactory.getDept(), 1L);
        UserBox userBox = UserBoxFactory.getUserBox(user, 1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);
        UserBoxSong userBoxSong = UserBoxSongFactory.getUserBoxSong(songDetail, userBox, 1L);
        List<UserBoxSong> userBoxSongs = new ArrayList<>();
        userBoxSongs.add(userBoxSong);

        doReturn(userBoxSongs.stream()
        .map(song -> new UserBoxSongDto.UserBoxSongResponse(song.getId(), song.getSongDetail().getId(), song.getSongDetail().getTitle(), song.getCreateDate()))
        .collect(Collectors.toList())).when(userBoxSongService).findAllUserBoxSong(anyLong(), anyLong(), anyString(), anyString(), anyInt(), anyInt());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("order", order);
        params.add("sort", sort);
        params.add("size", String.valueOf(size));
        params.add("offset", String.valueOf(offset));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/userbox/1/songs")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]['song_detail_id']").value(userBoxSongs.get(0).getSongDetail().getId()))
                .andExpect(jsonPath("$[0]['title']").value(userBoxSongs.get(0).getSongDetail().getTitle()))
                .andReturn();

        //verify
        verify(userBoxSongService, times(1)).findAllUserBoxSong(anyLong(), anyLong(), anyString(), anyString(), anyInt(), anyInt());
    }

}
