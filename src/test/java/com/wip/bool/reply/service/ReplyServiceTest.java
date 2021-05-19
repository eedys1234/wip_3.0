package com.wip.bool.reply.service;

import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.BoardRepository;
import com.wip.bool.board.domain.Reply;
import com.wip.bool.board.domain.ReplyRepository;
import com.wip.bool.board.dto.ReplyDto;
import com.wip.bool.board.service.ReplyService;
import com.wip.bool.cmmn.board.BoardFactory;
import com.wip.bool.cmmn.reply.ReplyFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReplyServiceTest {

    @InjectMocks
    private ReplyService replyService;

    @Mock
    private ReplyRepository replyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BoardRepository boardRepository;

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

    private Reply getReply(Board board, User user) {
        Reply reply = ReplyFactory.getReply(board, user);
        ReflectionTestUtils.setField(reply, "id", 1L);
        return reply;
    }

    private List<ReplyDto.ReplyResponse> getRepliesByBoard(Board board, User user) {
        return ReplyFactory.getRepliesByBoard(board, user);
    }

    private List<ReplyDto.ReplyResponse> getRepliesByReplies(Board board, User user) {
        return ReplyFactory.getRepliesByReplies(board, user);
    }

    @DisplayName("댓글 추가 by 게시물")
    @Test
    public void 댓글_추가_by게시물_Service() throws Exception {

        //given
        User user = getUser();
        Board board = getBoard(user);
        Reply reply = getReply(board, user);

        ReplyDto.ReplySaveRequest requestDto = new ReplyDto.ReplySaveRequest();
        ReflectionTestUtils.setField(requestDto, "content", "테스트 댓글");

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(board)).when(boardRepository).findById(any(Long.class));
        doReturn(reply).when(replyRepository).save(any(Reply.class));
        Long id = replyService.saveReply(user.getId(), board.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(reply.getId());

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(boardRepository, times(1)).findById(any(Long.class));
        verify(replyRepository, times(1)).save(any(Reply.class));
    }

    @DisplayName("댓글 추가 by 댓글")
    @Test
    public void 댓글_추가_by댓글_Service() throws Exception {

        //given
        User user = getUser();
        Board board = getBoard(user);
        Reply parentReply = getReply(board, user);
        Reply childReply = getReply(board, user);
        ReflectionTestUtils.setField(childReply, "id", 2L);

        ReplyDto.ReplySaveRequest requestDto = new ReplyDto.ReplySaveRequest();
        ReflectionTestUtils.setField(requestDto, "content", "테스트 댓글");
        ReflectionTestUtils.setField(requestDto, "parentId", parentReply.getId());

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(board)).when(boardRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(parentReply)).when(replyRepository).findById(any(Long.class));
        doReturn(childReply).when(replyRepository).save(any(Reply.class));
        Long id = replyService.saveReply(user.getId(), board.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(childReply.getId());

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(boardRepository, times(1)).findById(any(Long.class));
        verify(replyRepository, times(1)).findById(any(Long.class));
        verify(replyRepository, times(1)).save(any(Reply.class));
    }

    @DisplayName("댓글 리스트 조회 by 게시물")
    @Test
    public void 댓글_리스트_조회_by게시물_Service() throws Exception {

        //given
        int size = 10;
        int offset = 0;
        User user = getUser();
        Board board = getBoard(user);
        List<ReplyDto.ReplyResponse> repliesByBoard = getRepliesByBoard(board, user);

        //when
        doReturn(repliesByBoard).when(replyRepository).findAllByBoard(any(Long.class), any(Integer.class), any(Integer.class));
        List<ReplyDto.ReplyResponse> values = replyService.getsByBoard(board.getId(), size, offset);

        //then
        assertThat(values.size()).isEqualTo(repliesByBoard.size());

        //verify
        verify(replyRepository, times(1)).findAllByBoard(any(Long.class), any(Integer.class), any(Integer.class));
    }

    @DisplayName("댓글 리스트 조회 by 댓글")
    @Test
    public void 댓글_리스트_조회_by댓글_Service() throws Exception {

        //given
        int size = 10;
        int offset = 0;
        User user = getUser();
        Board board = getBoard(user);
        List<ReplyDto.ReplyResponse> repliesByReply = getRepliesByReplies(board, user);

        //when
        doReturn(repliesByReply).when(replyRepository).findAllByReply(any(Long.class), any(Integer.class), any(Integer.class));
        List<ReplyDto.ReplyResponse> values = replyService.getsByReply(repliesByReply.get(0).getReplyId(), size, offset);

        //then
        assertThat(values.size()).isEqualTo(1); // 0 depth
        assertThat(values.get(0).getNodes().size()).isEqualTo(2); // 1 depth
        assertThat(values.get(0).getNodes().get(1).getNodes().size()).isEqualTo(2); // 2 depth

        //verify
        verify(replyRepository, times(1)).findAllByReply(any(Long.class), any(Integer.class), any(Integer.class));
    }

    @DisplayName("댓글 삭제")
    @Test
    public void 댓글_삭제_Service() throws Exception {

        //given
        User user = getUser();
        Board board = getBoard(user);
        Reply parentReply = getReply(board, user);
        Reply childReply = getReply(board, user);
        ReflectionTestUtils.setField(childReply, "id", 2L);
        childReply.updateParentReply(parentReply);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(parentReply)).when(replyRepository).findById(any(Long.class), any(Long.class));
        doReturn(1L).when(replyRepository).delete(any(Reply.class));
        Long resValue = replyService.deleteReply(user.getId(), parentReply.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(replyRepository, times(1)).findById(any(Long.class), any(Long.class));
        verify(replyRepository, times(1)).delete(any(Reply.class));

    }
}
