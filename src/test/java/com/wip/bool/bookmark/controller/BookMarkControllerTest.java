package com.wip.bool.bookmark.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.bookmark.domain.BookMark;
import com.wip.bool.bookmark.dto.BookMarkDto;
import com.wip.bool.bookmark.service.BookMarkService;
import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.cmmn.bible.WordsMasterFactory;
import com.wip.bool.cmmn.bookmark.BookMarkFactory;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.music.song.SongDetailFactory;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.music.guitar.domain.GuitarCode;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookMarkControllerTest {

    @InjectMocks
    private BookMarkController bookMarkController;

    @Mock
    private BookMarkService bookMarkService;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() throws Exception {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(bookMarkController).build();
    }

    @DisplayName("즐겨찾기 추가")
    @Test
    public void 즐겨찾기_추가_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);
        BookMark bookMark = BookMarkFactory.getBookMark(user, songDetail, 1L);

        BookMarkDto.BookMarkSaveRequest requestDto = new BookMarkDto.BookMarkSaveRequest();
        ReflectionTestUtils.setField(requestDto, "songDetailId", songDetail.getId());
        doReturn(bookMark.getId()).when(bookMarkService).saveBookMark(anyLong(), any(BookMarkDto.BookMarkSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookmark")
                                                                                .header("userId", user.getId())
                                                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();
        assertThat(id).isEqualTo(bookMark.getId());

        //verify
        verify(bookMarkService, times(1)).saveBookMark(anyLong(), any(BookMarkDto.BookMarkSaveRequest.class));
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    public void 즐겨찾기_삭제_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);
        BookMark bookMark = BookMarkFactory.getBookMark(user, songDetail, 1L);

        doReturn(1L).when(bookMarkService).deleteBookMark(anyLong(), anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/bookmark/1")
                                                                                .header("userId", user.getId())
                                                                                .contentType(MediaType.APPLICATION_JSON_VALUE));
        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long resValue = response.getResult();
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(bookMarkService, times(1)).deleteBookMark(anyLong(), anyLong());
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    public void 즐겨찾기_조회_Controller() throws Exception {

        //given
        int size = 10;
        int offset = 0;
        String sort = "TITLE";
        String order = "ASC";

        User user = UserFactory.getNormalUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);

        List<SongDetail> songDetails = SongDetailFactory.getSongDetailsWithId(songMaster, guitarCode, wordsMaster);
        List<BookMark> bookMarks = BookMarkFactory.getBookMarksWithId(user, songDetails);

        doReturn(bookMarks.stream().map(bookMark -> new BookMarkDto.BookMarkResponse(bookMark.getId(), bookMark.getSongDetail().getId(),
                bookMark.getSongDetail().getTitle(), bookMark.getSongDetail().getGuitarCode().getCode(), bookMark.getCreateDate()))
                .collect(Collectors.toList())).when(bookMarkService).findBookMarks(anyLong(), anyString(), anyString(), anyInt(), anyInt());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("sort", sort);
        params.add("order", order);
        params.add("size", String.valueOf(size));
        params.add("offset", String.valueOf(offset));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookmarks")
                                                                                    .header("userId", user.getId())
                                                                                    .params(params)
                                                                                    .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<List<BookMarkDto.BookMarkResponse>> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<List<BookMarkDto.BookMarkResponse>>>() {});
        List<BookMarkDto.BookMarkResponse> values = response.getResult();
        assertThat(values.size()).isEqualTo(bookMarks.size());

        //verify
        verify(bookMarkService, times(1)).findBookMarks(anyLong(), anyString(), anyString(), anyInt(), anyInt());
    }
}
