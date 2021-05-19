package com.wip.bool.reply.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.board.controller.ReplyController;
import com.wip.bool.board.domain.Board;
import com.wip.bool.board.dto.ReplyDto;
import com.wip.bool.board.service.ReplyService;
import com.wip.bool.cmmn.board.BoardFactory;
import com.wip.bool.cmmn.reply.ReplyFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class ReplyControllerTest {

    @InjectMocks
    private ReplyController replyController;

    @Mock
    private ReplyService replyService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private User getUser() {
        User user = UserFactory.getNormalUser();
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private Board getBoard(User user) {
        Board board = BoardFactory.getBoard(user);
        ReflectionTestUtils.setField(board, "id", 1L);
        return board;
    }

    private List<ReplyDto.ReplyResponse> getRepliesByBoards(Board board, User user) {
        return ReplyFactory.getRepliesByBoard(board, user);
    }

    private List<ReplyDto.ReplyResponse> getRepliesByReplies(Board board, User user) {
        return ReplyFactory.getRepliesByReplies(board, user);
    }

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(replyController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("댓글 추가 by 게시물")
    @Test
    public void 댓글_추가_by게시물_Controller() throws Exception {

        //given
        ReplyDto.ReplySaveRequest requestDto = new ReplyDto.ReplySaveRequest();
        ReflectionTestUtils.setField(requestDto, "content", "테스트 댓글");

        doReturn(1L).when(replyService).saveReply(any(Long.class), any(Long.class), any(ReplyDto.ReplySaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/board/1/reply")
                                                                                    .contentType(MediaType.APPLICATION_JSON)
                                                                                    .content(objectMapper.writeValueAsString(requestDto))
                                                                                    .header("userId", 1L));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(id).isEqualTo(1L);

        //verify
        verify(replyService, times(1)).saveReply(any(Long.class), any(Long.class), any(ReplyDto.ReplySaveRequest.class));

    }

    @DisplayName("댓글 추가 by 댓글")
    @Test
    public void 댓글_추가_by댓글_Controller() throws Exception {

        //given
        ReplyDto.ReplySaveRequest requestDto = new ReplyDto.ReplySaveRequest();
        ReflectionTestUtils.setField(requestDto, "content", "테스트 댓글");
        ReflectionTestUtils.setField(requestDto, "parentId", 2L);

        doReturn(1L).when(replyService).saveReply(any(Long.class), any(Long.class), any(ReplyDto.ReplySaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/board/1/reply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .header("userId", 1L));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(id).isEqualTo(1L);

        //verify
        verify(replyService, times(1)).saveReply(any(Long.class), any(Long.class), any(ReplyDto.ReplySaveRequest.class));

    }

    @DisplayName("댓글 리스트 조회 by 게시물")
    @Test
    public void 댓글_리스트_조회_by게시물_Controller() throws Exception {

        //given
        User user = getUser();
        Board board = getBoard(user);
        List<ReplyDto.ReplyResponse> repliesByBoards = getRepliesByBoards(board, user);
        doReturn(repliesByBoards).when(replyService).getsByBoard(any(Long.class), any(Integer.class), any(Integer.class));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("size", "10");
        params.add("offset", "0");

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/board/1/reply")
                                                                                    .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                .andExpect(status().isOk())
                                                .andExpect(jsonPath("$").isArray())
                                                .andReturn();

        //verify
        verify(replyService, times(1)).getsByBoard(any(Long.class), any(Integer.class), any(Integer.class));
    }

    @DisplayName("댓글 리스트 조회 by 댓글")
    @Test
    public void 댓글_리스트_조회_by댓글_Controller() throws Exception {

        //given
        User user = getUser();
        Board board = getBoard(user);
        List<ReplyDto.ReplyResponse> repliesByBoards = getRepliesByReplies(board, user);
        doReturn(repliesByBoards).when(replyService).getsByBoard(any(Long.class), any(Integer.class), any(Integer.class));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("size", "10");
        params.add("offset", "0");

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/board/1/reply")
                .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn();

        //verify
        verify(replyService, times(1)).getsByBoard(any(Long.class), any(Integer.class), any(Integer.class));
    }

    @DisplayName("댓글 삭제")
    @Test
    public void 댓글_삭제_Controller() throws Exception {

        //given
        doReturn(1L).when(replyService).deleteReply(any(Long.class), any(Long.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/board/1/reply/1")
                                                                                    .header("userId", 1L));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long resValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(replyService, times(1)).deleteReply(any(Long.class), any(Long.class));

    }

}
