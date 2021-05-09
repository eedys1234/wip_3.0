package com.wip.bool.reply.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.board.controller.ReplyController;
import com.wip.bool.board.dto.ReplyDto;
import com.wip.bool.board.service.ReplyService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class ReplyControllerTest {

    @InjectMocks
    private ReplyController replyController;

    @Mock
    private ReplyService replyService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;


//    private Board getBoard() {
//
//    }
//
//    private List<ReplyDto.ReplyResponse> getRepliesByBoards() {
//
//    }
//
//    private List<ReplyDto.ReplyResponse> getRepliesByReplies() {
//
//    }

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(replyController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("댓글 추가 by 게시물 Controller")
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

    @DisplayName("댓글 추가 by 댓글 Controller")
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

    @DisplayName("댓글 리스트 조회 by 게시물 Controller")
    @Test
    public void 댓글_리스트_조회_by게시물_Controller() throws Exception {

//        //given
//        List<ReplyDto.ReplyResponse> repliesByBoards = getRepliesByBoards();
//        doReturn(repliesByBoards).when(replyService).getsByBoard(any(Long.class), any(Integer.class), any(Integer.class));
//
//        //when
//        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/board/1/reply"));
//
//        //then
//        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
//
//        //verify
//        verify(replyService, times(1)).getsByBoard(any(Long.class), any(Integer.class), any(Integer.class));
    }

    @DisplayName("댓글 리스트 조회 by 댓글 Controller")
    @Test
    public void 댓글_리스트_조회_by댓글_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser();

        //when

        //then

        //verify
    }


}
