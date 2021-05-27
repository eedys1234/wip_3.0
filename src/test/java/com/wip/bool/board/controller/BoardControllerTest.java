package com.wip.bool.board.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.BoardType;
import com.wip.bool.board.dto.BoardDto;
import com.wip.bool.board.service.BoardService;
import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.cmmn.board.BoardFactory;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BoardControllerTest {

    @InjectMocks
    private BoardController boardController;

    @Mock
    private BoardService boardService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();
        objectMapper = new ObjectMapper();
    }

    private List<BoardDto.BoardResponse> getDetailBoards(User user) {
        List<BoardDto.BoardResponse> detailBoards = new ArrayList<>();
        String title = "테스트 게시물";
        String content = "게시물내용";
        BoardType boardType = BoardType.BOARD;
        Board board = Board.createBoard(title, content, boardType, user);
        ReflectionTestUtils.setField(board, "id", 1L);

        detailBoards.add(new BoardDto.BoardResponse(board));
        return detailBoards;
    }

    @DisplayName("게시물 저장")
    @Test
    public void 게시물_저장_Controller() throws Exception {

        //given
        String title = "테스트 게시물";
        String content = "게시물내용";
        String boardType = "BOARD";

        User user = UserFactory.getNormalUser(1L);
        Board board = BoardFactory.getBoard(user, 1L);

        BoardDto.BoardSaveRequest requestDto = new BoardDto.BoardSaveRequest();
        ReflectionTestUtils.setField(requestDto, "title", title);
        ReflectionTestUtils.setField(requestDto, "content", content);
        ReflectionTestUtils.setField(requestDto, "boardType", boardType);

        doReturn(board.getId()).when(boardService).saveBoard(anyLong(), any(BoardDto.BoardSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/board")
                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                    .header("userId", user.getId())
                                                    .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();
        assertThat(id).isEqualTo(board.getId());

        //verify
        verify(boardService, times(1)).saveBoard(anyLong(), any(BoardDto.BoardSaveRequest.class));
    }

    @DisplayName("게시물 리스트 조회")
    @Test
    public void 게시물_리스트_조회_Controller() throws Exception {

        //given
        String boardType = "BOARD";
        int size = 10;
        int offset = 0;

        User user = UserFactory.getNormalUser(1L);
        List<Board> boards = BoardFactory.getBoardsWithId(user);

        doReturn(boards.stream().map(board -> new BoardDto.BoardSimpleResponse(board.getId(), board.getTitle(), board.getIsDeleted(), board.getBoardType()))
        .collect(Collectors.toList())).when(boardService).findBoards(anyString(), anyInt(), anyInt());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("board", boardType);
        params.add("size", String.valueOf(size));
        params.add("offset", String.valueOf(offset));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/boards")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                .andExpect(status().isOk())
                                                .andExpect(jsonPath("$.result").isArray())
                                                .andExpect(jsonPath("$.result[0].board_id").value(boards.get(0).getId()))
                                                .andExpect(jsonPath("$.result[0].title").value("숨김처리된 게시글입니다."))
                                                .andReturn();

        //verify
        verify(boardService, times(1)).findBoards(anyString(), anyInt(), anyInt());
    }

    @DisplayName("게시물 상세 조회")
    @Test
    public void 게시물_상세_조회_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        List<BoardDto.BoardResponse> detailBoards = getDetailBoards(user);
        doReturn(detailBoards.get(0)).when(boardService).findDetailBoard(anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/board/1"));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.board_id").value(detailBoards.get(0).getBoardId()))
                .andExpect(jsonPath("$.result.title").value(detailBoards.get(0).getTitle()))
                .andExpect(jsonPath("$.result.content").value(detailBoards.get(0).getContent()))
                .andExpect(jsonPath("$.result.board_type").value(String.valueOf(detailBoards.get(0).getBoardType())))
                .andReturn();

        //verify
        verify(boardService, times(1)).findDetailBoard(anyLong());
    }

    @DisplayName("게시물 삭제")
    @Test
    public void 게시물_삭제_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        doReturn(1L).when(boardService).deleteBoard(anyLong(), anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/board/1")
        .header("userId", user.getId()));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                .andExpect(status().isOk())
                                                .andReturn();

        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long resValue = response.getResult();
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(boardService, times(1)).deleteBoard(anyLong(), anyLong());

    }

    @DisplayName("게시물 숨김처리")
    @Test
    public void 게시물_숨김처리_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        doReturn(1L).when(boardService).hiddenBoard(anyLong(), anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/board/1/hidden")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                .andExpect(status().isOk())
                                                .andReturn();

        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long resValue = response.getResult();
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(boardService, times(1)).hiddenBoard(anyLong(), anyLong());
    }
}
