package com.wip.bool.music.mp3.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.cmmn.bible.WordsMasterFactory;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.music.mp3.SongMP3Factory;
import com.wip.bool.cmmn.music.song.SongDetailFactory;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.mp3.domain.SongMP3;
import com.wip.bool.music.mp3.service.SongMP3Service;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.file.FileSystems;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SongMP3ControllerTest {

    @Value("${spring.mp3.path}")
    private String mp3Path;

    @InjectMocks
    private SongMP3Controller songMP3Controller;

    @Mock
    private SongMP3Service songMP3Service;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(songMP3Controller).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("mp3 파일 추가")
    @Test
    public void mp3_파일_추가_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);

        SongMP3 songMP3 = SongMP3Factory.getSongMP3(songDetail, mp3Path, SongMP3Factory.orgFileName, SongMP3Factory.byteString.getBytes(), 1L);
        byte[] mp3FileBytes = Files.readAllBytes(FileSystems.getDefault().getPath("src/test/resources/mp3", SongMP3Factory.orgFileName));

        doReturn(songMP3.getId()).when(songMP3Service).saveSongMP3(anyLong(), anyLong(), anyString(), any());

        MockMultipartFile multipartFile = new MockMultipartFile("mp3File", SongMP3Factory.orgFileName, MediaType.MULTIPART_FORM_DATA_VALUE, mp3FileBytes);

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/music/song-detail/1/mp3")
        .file(multipartFile)
        .header("userId", user.getId())
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();
        assertThat(id).isEqualTo(songMP3.getId());

        //verify
        verify(songMP3Service, times(1)).saveSongMP3(anyLong(), anyLong(), anyString(), any());
    }

    @DisplayName("mp3 파일 삭제")
    @Test
    public void mp3_파일_삭제_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        doReturn(1L).when(songMP3Service).deleteSongMP3(anyLong(), anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/music/song-detail/1/mp3/1")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long resValue = response.getResult();
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(songMP3Service, times(1)).deleteSongMP3(anyLong(), anyLong());
    }

    @DisplayName("mp3 파일 가져오기")
    @Test
    public void mp3_파일_가져오기_Controller() throws Exception {

        //given
        byte[] mp3FileBytes = Files.readAllBytes(FileSystems.getDefault().getPath("src/test/resources/mp3", SongMP3Factory.orgFileName));
        doReturn(mp3FileBytes).when(songMP3Service).getFile(anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/music/song-detail/1/mp3/1")
        .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isString())
                .andReturn();

        //verify
        verify(songMP3Service, times(1)).getFile(anyLong());
    }


}


