package com.wip.bool.reply.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.board.controller.ReplyController;
import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.Reply;
import com.wip.bool.board.dto.ReplyDto;
import com.wip.bool.board.service.ReplyService;
import com.wip.bool.cmmn.ApiResponse;
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
import java.util.stream.Collectors;

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

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(replyController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("댓글 추가 by 게시물")
    @Test
    public void 댓글_추가_by게시물_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        Board board = BoardFactory.getBoard(user, 1L);
        Reply reply = ReplyFactory.getReply(board, user, 1L);

        ReplyDto.ReplySaveRequest requestDto = new ReplyDto.ReplySaveRequest();
        ReflectionTestUtils.setField(requestDto, "content", reply.getContent());

        doReturn(1L).when(replyService).saveReply(anyLong(), anyLong(), any(ReplyDto.ReplySaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/board/1/reply")
                                                                                    .header("userId", user.getId())
                                                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                    .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();
        assertThat(id).isEqualTo(reply.getId());

        //verify
        verify(replyService, times(1)).saveReply(anyLong(), anyLong(), any(ReplyDto.ReplySaveRequest.class));

    }

    @DisplayName("댓글 추가 by 댓글")
    @Test
    public void 댓글_추가_by댓글_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        Board board = BoardFactory.getBoard(user, 1L);
        Reply parentReply = ReplyFactory.getReply(board, user, 2L);
        Reply childReply = ReplyFactory.getReply(board, user, 3L);

        ReplyDto.ReplySaveRequest requestDto = new ReplyDto.ReplySaveRequest();
        ReflectionTestUtils.setField(requestDto, "content", childReply.getContent());
        ReflectionTestUtils.setField(requestDto, "parentId", parentReply.getId());

        doReturn(childReply.getId()).when(replyService).saveReply(anyLong(), anyLong(), any(ReplyDto.ReplySaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/board/1/reply")
                .header("userId", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();
        assertThat(id).isEqualTo(childReply.getId());

        //verify
        verify(replyService, times(1)).saveReply(anyLong(), anyLong(), any(ReplyDto.ReplySaveRequest.class));

    }

    @DisplayName("댓글 리스트 조회 by 게시물")
    @Test
    public void 댓글_리스트_조회_by게시물_Controller() throws Exception {

        //given
        int size = 10;
        int offset = 0;

        User user = UserFactory.getNormalUser(1L);
        Board board = BoardFactory.getBoard(user, 1L);
        List<Reply> replies = ReplyFactory.getRepliesWithId(board, user);

        doReturn(replies.stream()
        .map(reply -> new ReplyDto.ReplyResponse(reply, null))
        .collect(Collectors.toList())).when(replyService).getsByBoard(any(Long.class), any(Integer.class), any(Integer.class));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("size", String.valueOf(size));
        params.add("offset", String.valueOf(offset));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/board/1/reply")
                                                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                    .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                .andExpect(status().isOk())
                                                .andExpect(jsonPath("$.result").isArray())
                                                .andExpect(jsonPath("$.result[0]['reply_id']").value(replies.get(0).getId()))
                                                .andReturn();

        //verify
        verify(replyService, times(1)).getsByBoard(anyLong(), anyInt(), anyInt());
    }

    @DisplayName("댓글 리스트 조회 by 댓글")
    @Test
    public void 댓글_리스트_조회_by댓글_Controller() throws Exception {

        //given
        int size = 10;
        int offset = 0;
        User user = UserFactory.getNormalUser(1L);
        Board board = BoardFactory.getBoard(user, 1L);
        List<ReplyDto.ReplyResponse> repliesByReply = ReplyFactory.getRepliesByReplies(board, user);

        doReturn(repliesByReply).when(replyService).getsByBoard(anyLong(), anyInt(), anyInt());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("size", String.valueOf(size));
        params.add("offset", String.valueOf(offset));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/board/1/reply")
                                                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                    .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result.length()").value(10))
                .andReturn();

        //verify
        verify(replyService, times(1)).getsByBoard(anyLong(), anyInt(), anyInt());
    }

    @DisplayName("댓글 삭제")
    @Test
    public void 댓글_삭제_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        doReturn(1L).when(replyService).deleteReply(anyLong(), anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/board/1/reply/1")
                                                                                    .header("userId", user.getId()));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long resValue = response.getResult();
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(replyService, times(1)).deleteReply(anyLong(), anyLong());

    }

}
