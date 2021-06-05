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
import com.wip.bool.user.domain.Role;
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
import java.util.stream.Collectors;

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

    @DisplayName("댓글 추가 by 게시물")
    @Test
    public void 댓글_추가_by게시물_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        Board board = BoardFactory.getBoard(user, 1L);
        Reply reply = ReplyFactory.getReply(board, user, 1L);

        ReplyDto.ReplySaveRequest requestDto = new ReplyDto.ReplySaveRequest();
        ReflectionTestUtils.setField(requestDto, "content", reply.getContent());

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(board)).when(boardRepository).findById(anyLong());
        doReturn(reply).when(replyRepository).save(any(Reply.class));
        Long id = replyService.saveReply(user.getId(), board.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(reply.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(boardRepository, times(1)).findById(anyLong());
        verify(replyRepository, times(1)).save(any(Reply.class));
    }

    @DisplayName("댓글 추가 by 댓글")
    @Test
    public void 댓글_추가_by댓글_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        Board board = BoardFactory.getBoard(user, 1L);
        Reply parentReply = ReplyFactory.getReply(board, user, 1L);
        Reply childReply = ReplyFactory.getReply(board, user, 2L);

        ReplyDto.ReplySaveRequest requestDto = new ReplyDto.ReplySaveRequest();
        ReflectionTestUtils.setField(requestDto, "content", parentReply.getContent());
        ReflectionTestUtils.setField(requestDto, "parentId", parentReply.getId());

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(board)).when(boardRepository).findById(anyLong());
        doReturn(Optional.ofNullable(parentReply)).when(replyRepository).findById(anyLong());
        doReturn(childReply).when(replyRepository).save(any(Reply.class));
        Long id = replyService.saveReply(user.getId(), board.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(childReply.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(boardRepository, times(1)).findById(anyLong());
        verify(replyRepository, times(1)).findById(anyLong());
        verify(replyRepository, times(1)).save(any(Reply.class));
    }

    @DisplayName("댓글 리스트 조회 by 게시물")
    @Test
    public void 댓글_리스트_조회_by게시물_Service() throws Exception {

        //given
        int size = 10;
        int offset = 0;

        User user = UserFactory.getNormalUser(1L);
        Board board = BoardFactory.getBoard(user, 1L);

        List<Reply> replies = ReplyFactory.getRepliesWithId(board, user);

        //when
        doReturn(replies.stream()
        .map(reply -> new ReplyDto.ReplyResponse(reply))
        .collect(Collectors.toList())).when(replyRepository).findAllByBoard(anyLong(), anyInt(), anyInt());

        List<ReplyDto.ReplyResponse> values = replyService.getsByBoard(board.getId(), size, offset);

        //then
        assertThat(values.size()).isEqualTo(replies.size());
        assertThat(values).extracting(ReplyDto.ReplyResponse::getContent).containsAll(replies.stream().map(Reply::getContent).collect(Collectors.toList()));

        //verify
        verify(replyRepository, times(1)).findAllByBoard(anyLong(), anyInt(), anyInt());
    }

    @DisplayName("댓글 리스트 조회 by 댓글")
    @Test
    public void 댓글_리스트_조회_by댓글_Service() throws Exception {

        //given
        int size = 10;
        int offset = 0;

        User user = UserFactory.getNormalUser(1L);
        Board board = BoardFactory.getBoard(user, 1L);

        List<ReplyDto.ReplyResponse> repliesByReply = ReplyFactory.getRepliesByReplies(board, user);

        //when
        doReturn(repliesByReply).when(replyRepository).findAllByReply(anyLong(), anyInt(), anyInt());
        List<ReplyDto.ReplyResponse> values = replyService.getsByReply(repliesByReply.get(0).getReplyId(), size, offset);

        //then
        assertThat(values.size()).isEqualTo(1); // 0 depth
        assertThat(values.get(0).getNodes().size()).isEqualTo(2); // 1 depth
        assertThat(values.get(0).getNodes().get(1).getNodes().size()).isEqualTo(2); // 2 depth

        //verify
        verify(replyRepository, times(1)).findAllByReply(anyLong(), anyInt(), anyInt());
    }

    @DisplayName("댓글 삭제")
    @Test
    public void 댓글_삭제_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        Board board = BoardFactory.getBoard(user, 1L);
        Reply parentReply = ReplyFactory.getReply(board, user, 1L);
        Reply childReply = ReplyFactory.getReply(board, user, 2L);

        childReply.updateParentReply(parentReply);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(parentReply)).when(replyRepository).findById(anyLong(), anyLong(), any(Role.class));
        doReturn(1L).when(replyRepository).delete(any(Reply.class));
        Long resValue = replyService.deleteReply(user.getId(), parentReply.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(replyRepository, times(1)).findById(anyLong(), anyLong(), any(Role.class));
        verify(replyRepository, times(1)).delete(any(Reply.class));

    }
}
