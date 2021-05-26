package com.wip.bool.music.song.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.cmmn.bible.WordsMasterFactory;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.music.song.SongDetailFactory;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.music.song.dto.SongDetailDto;
import com.wip.bool.music.song.service.SongDetailService;
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
public class SongDetailControllerTest {

    @InjectMocks
    private SongDetailController songDetailController;

    @Mock
    private SongDetailService songDetailService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(songDetailController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("곡 추가")
    @Test
    public void 곡_추가_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);
        doReturn(songDetail.getId()).when(songDetailService).saveSong(anyLong(), any(SongDetailDto.SongDetailSaveRequest.class));

        SongDetailDto.SongDetailSaveRequest requestDto = new SongDetailDto.SongDetailSaveRequest();
        ReflectionTestUtils.setField(requestDto, "title", songDetail.getTitle());
        ReflectionTestUtils.setField(requestDto, "lyrics", songDetail.getLyrics());
        ReflectionTestUtils.setField(requestDto, "codeId", songDetail.getSongMaster().getId());
        ReflectionTestUtils.setField(requestDto, "guitarCodeId", songDetail.getGuitarCode().getId());
        ReflectionTestUtils.setField(requestDto, "wordsMasterId", songDetail.getWordsMaster().getId());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/music/song-detail")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();
        assertThat(id).isEqualTo(songDetail.getId());

        //verify
        verify(songDetailService, times(1)).saveSong(anyLong(), any(SongDetailDto.SongDetailSaveRequest.class));
    }

    @DisplayName("곡 수정")
    @Test
    public void 곡_수정_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);

        SongDetailDto.SongDetailUpdateRequest requestDto = new SongDetailDto.SongDetailUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "title", songDetail.getTitle());
        ReflectionTestUtils.setField(requestDto, "lyrics", songDetail.getLyrics());
        ReflectionTestUtils.setField(requestDto, "codeId", songDetail.getSongMaster().getId());
        ReflectionTestUtils.setField(requestDto, "guitarCodeId", songDetail.getGuitarCode().getId());
        ReflectionTestUtils.setField(requestDto, "wordsMasterId", songDetail.getWordsMaster().getId());

        doReturn(songDetail.getId()).when(songDetailService).updateSong(anyLong(), anyLong(), any(SongDetailDto.SongDetailUpdateRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/music/song-detail/1")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();
        assertThat(id).isEqualTo(songDetail.getId());

        //verify
        verify(songDetailService, times(1)).updateSong(anyLong(), anyLong(), any(SongDetailDto.SongDetailUpdateRequest.class));
    }

    @DisplayName("곡 삭제")
    @Test
    public void 곡_삭제_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        doReturn(1L).when(songDetailService).deleteSong(anyLong(), anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/music/song-detail/1")
        .header("userId", user.getId())
        .content(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long resValue = response.getResult();
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(songDetailService, times(1)).deleteSong(anyLong(), anyLong());
    }

    @DisplayName("곡 리스트 간편 조회")
    @Test
    public void 곡_리스트_간편_조회_Controller() throws Exception {

        //given
        String order = "ASC";
        String sort = "TITLE";
        int size = 10;
        int offset = 0;
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);

        List<SongDetail> songDetails = SongDetailFactory.getSongDetailsWithId(songMaster, guitarCode, wordsMaster);

        doReturn(songDetails.stream()
                .map(songDetail -> new SongDetailDto.SongDetailSimpleResponse(songDetail.getId(), songDetail.getTitle(), songDetail.getGuitarCode().getId()))
                .collect(Collectors.toList())).when(songDetailService).findAll(anyLong(), anyString(), anyString(), anyInt(), anyInt());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("songMasterId", String.valueOf(songMaster.getId()));
        params.add("order", order);
        params.add("sort", sort);
        params.add("size", String.valueOf(size));
        params.add("offset", String.valueOf(offset));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/music/song-details")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0]['title']").value(songDetails.get(0).getTitle()))
                .andExpect(jsonPath("$.result[0]['id']").value(songDetails.get(0).getId()))
                .andReturn();


        //verify
        verify(songDetailService, times(1)).findAll(anyLong(), anyString(), anyString(), anyInt(), anyInt());
    }

    @DisplayName("곡 상세조회")
    @Test
    public void 곡_상세조회_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);

        doReturn(new SongDetailDto.SongDetailResponse(songDetail)).when(songDetailService).findDetailOne(anyLong(), anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/music/song-detail/1")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isMap())
                .andExpect(jsonPath("$.result.id").value(songDetail.getId()))
                .andExpect(jsonPath("$.result.title").value(songDetail.getTitle()))
                .andExpect(jsonPath("$.result.song_master_id").value(songDetail.getSongMaster().getId()))
                .andExpect(jsonPath("$.result.guitar_code_id").value(songDetail.getGuitarCode().getId()))
                .andReturn();

        //verify
        verify(songDetailService, times(1)).findDetailOne(anyLong(), anyLong());
    }

    @DisplayName("곡 검색")
    @Test
    public void 곡_검색_Controller() throws Exception {

        //given
        String keyword = "테스트";
        List<String> songTitles = SongDetailFactory.getSongDetailTitle();

        doReturn(songTitles.stream()
        .map(title -> new SongDetailDto.SongDetailSimpleResponse(title))
        .collect(Collectors.toList())).when(songDetailService).search(anyString());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/music/song-details/search")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .param("keyword", keyword));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0]['title']").value(songTitles.get(0)))
                .andReturn();
        
        //verify
        verify(songDetailService, times(1)).search(anyString());
    }
}
