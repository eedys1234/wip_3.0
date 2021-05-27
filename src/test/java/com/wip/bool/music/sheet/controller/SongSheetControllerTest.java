package com.wip.bool.music.sheet.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.cmmn.bible.WordsMasterFactory;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.music.sheet.SongSheetFactory;
import com.wip.bool.cmmn.music.song.SongDetailFactory;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.sheet.domain.SongSheet;
import com.wip.bool.music.sheet.service.SongSheetService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SongSheetControllerTest {

    @InjectMocks
    private SongSheetController songSheetController;

    @Mock
    private SongSheetService songSheetService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Value("${spring.images.path}")
    private String imagePath;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(songSheetController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("악보 추가")
    @Test
    public void 악보_추가_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);
        SongSheet songSheet = SongSheetFactory.getSongSheet(songDetail, imagePath, SongSheetFactory.orgFileName, SongSheetFactory.byteString.getBytes(), 1, 1L);

        doReturn(songSheet.getId()).when(songSheetService).saveSongSheet(anyLong(), anyLong(), anyString(), any());

        MockMultipartFile multipartFile = new MockMultipartFile("imagesFile", SongSheetFactory.orgFileName,
                MediaType.MULTIPART_FORM_DATA_VALUE, SongSheetFactory.byteString.getBytes());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/music/song-detail/1/sheet")
        .file(multipartFile)
        .header("userId", user.getId())
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                    .andExpect(status().isCreated())
                                                    .andExpect(jsonPath("$.result").isNumber())
                                                    .andReturn();

        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();
        assertThat(id).isEqualTo(songSheet.getId());

        //verify
        verify(songSheetService, times(1)).saveSongSheet(anyLong(), anyLong(), anyString(), any());
    }

    @DisplayName("악보 삭제")
    @Test
    public void 악보_삭제_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        doReturn(1L).when(songSheetService).deleteSongSheet(anyLong(), anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/music/song-detail/1/sheet/1")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long resValue = response.getResult();
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(songSheetService, times(1)).deleteSongSheet(anyLong(), anyLong());
    }
}
