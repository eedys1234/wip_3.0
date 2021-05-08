package com.wip.bool.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.BoardType;
import com.wip.bool.board.dto.BoardDto;
import com.wip.bool.board.service.BoardService;
import com.wip.bool.cmmn.status.DeleteStatus;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserType;
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

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();
        objectMapper = new ObjectMapper();
    }

    private User getUser() {

        String email = "test@gmail.com";
        String password = "test1234";
        String profiles = "";
        Role role = Role.ROLE_NORMAL;
        UserType userType = UserType.WIP;

        User user = User.createUser(email, password, profiles, userType, role);
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private Board getBoard(User user) {

        String title = "테스트 게시물";
        String content = "게시물 내용";
        BoardType boardType = BoardType.BOARD;

        Board board = Board.createBoard(title, content, boardType, user);
        ReflectionTestUtils.setField(board, "id", 1L);
        return board;
    }

    private List<BoardDto.BoardSimpleResponse> getSimpleBoards() {

        List<BoardDto.BoardSimpleResponse> simpleBoards = new ArrayList<>();
        BoardDto.BoardSimpleResponse board1 = new BoardDto.BoardSimpleResponse(1L, "테스트 게시물1", DeleteStatus.DELETE, BoardType.BOARD);
        BoardDto.BoardSimpleResponse board2 = new BoardDto.BoardSimpleResponse(2L, "테스트 게시물2", DeleteStatus.NORMAL, BoardType.BOARD);
        BoardDto.BoardSimpleResponse board3 = new BoardDto.BoardSimpleResponse(3L, "테스트 게시물3", DeleteStatus.NORMAL, BoardType.BOARD);
        BoardDto.BoardSimpleResponse board4 = new BoardDto.BoardSimpleResponse(4L, "테스트 게시물4", DeleteStatus.NORMAL, BoardType.BOARD);

        simpleBoards.add(board1);
        simpleBoards.add(board2);
        simpleBoards.add(board3);
        simpleBoards.add(board4);

        return simpleBoards;
    }

    private List<BoardDto.BoardResponse> getDetailBoards(User user) {
        List<BoardDto.BoardResponse> detailBoards = new ArrayList<>();

        String title = "테스트 게시물";
        String content = "게시물내용";
        BoardType boardType = BoardType.BOARD;
        Board board = Board.createBoard(title, content, boardType, user);
        ReflectionTestUtils.setField(board, "id", 1L);

        detailBoards.add(new BoardDto.BoardResponse(board, null));
        return detailBoards;
    }

    @DisplayName("게시물 저장")
    @Test
    public void saveBoard_Controller() throws Exception {

        //given
        String title = "테스트 게시물";
        String content = "게시물내용";
        String boardType = "BOARD";
        User user = getUser();
        Board board = getBoard(user);
        BoardDto.BoardSaveRequest requestDto = new BoardDto.BoardSaveRequest();
        ReflectionTestUtils.setField(requestDto, "title", title);
        ReflectionTestUtils.setField(requestDto, "content", content);
        ReflectionTestUtils.setField(requestDto, "boardType", boardType);

        doReturn(board.getId()).when(boardService).saveBoard(any(Long.class), any(BoardDto.BoardSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/board")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .header("userId", user.getId())
                                                    .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        Long resValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(resValue).isEqualTo(board.getId());

        //verify
        verify(boardService, times(1)).saveBoard(any(Long.class), any(BoardDto.BoardSaveRequest.class));
    }

    @DisplayName("게시물 리스트 조회")
    @Test
    public void 게시물_리스트_조회_Controller() throws Exception {

        //given
        User user = getUser();
        List<BoardDto.BoardSimpleResponse> boards = getSimpleBoards();
        doReturn(boards).when(boardService).findBoards(any(String.class), any(Integer.class), any(Integer.class));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("board", "BOARD");
        params.add("size", "10");
        params.add("offset", "0");

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/boards")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                .andExpect(status().isOk())
                                                .andExpect(jsonPath("$").isArray())
                                                .andExpect(jsonPath("$[0].boardId").value(1L))
                                                .andExpect(jsonPath("$[0].title").value("숨김처리된 게시글입니다."))
                                                .andReturn();

        //verify
        verify(boardService, times(1)).findBoards(any(String.class), any(Integer.class), any(Integer.class));
    }

    @DisplayName("게시물 상세 조회")
    @Test
    public void 게시물_상세_조회_Controller() throws Exception {

        //given
        User user = getUser();
        List<BoardDto.BoardResponse> detailBoards = getDetailBoards(user);
        doReturn(detailBoards.get(0)).when(boardService).findDetailBoard(any(Long.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/board/1"));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardId").value(detailBoards.get(0).getBoardId()))
                .andExpect(jsonPath("$.title").value(detailBoards.get(0).getTitle()))
                .andExpect(jsonPath("$.content").value(detailBoards.get(0).getContent()))
                .andExpect(jsonPath("$.boardType").value(String.valueOf(detailBoards.get(0).getBoardType())))
                .andReturn();

        //verify
        verify(boardService, times(1)).findDetailBoard(any(Long.class));
    }

    @DisplayName("게시물 삭제")
    @Test
    public void 게시물_삭제_Controller() throws Exception {

        //given
        doReturn(1L).when(boardService).deleteBoard(any(Long.class), any(Long.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/board/1")
        .header("userId", 1));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                .andExpect(status().isOk())
                                                .andReturn();

        Long resValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(boardService, times(1)).deleteBoard(any(Long.class), any(Long.class));

    }

    @DisplayName("게시물 숨김처리")
    @Test
    public void 게시물_숨김처리_Controller() throws Exception {

        //given
        doReturn(1L).when(boardService).hiddenBoard(any(Long.class), any(Long.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/board/1/hidden")
        .header("userId", 1L));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                .andExpect(status().isOk())
                                                .andReturn();


        Long resValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(boardService, times(1)).hiddenBoard(any(Long.class), any(Long.class));
    }
}
