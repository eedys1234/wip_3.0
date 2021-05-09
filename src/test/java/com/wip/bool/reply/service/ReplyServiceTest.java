package com.wip.bool.reply.service;

import com.wip.bool.board.domain.*;
import com.wip.bool.board.dto.ReplyDto;
import com.wip.bool.board.service.ReplyService;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.user.domain.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
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

    private User getUser() {

        String email = "test@gmail.com";
        String password = "test1234";
        String profiles = "";
        UserType userType = UserType.WIP;
        Role role = Role.ROLE_NORMAL;

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

    private Reply getReply(Board board, User user) {

        String content = "테스트 댓글";

        Reply reply = Reply.createReply(content, board, user);
        ReflectionTestUtils.setField(reply, "id", 1L);

        return reply;
    }

    private List<ReplyDto.ReplyResponse> getRepliesByBoard(Board board, User user) {

        Reply reply1 = getReply(board, user);
        ReflectionTestUtils.setField(reply1, "id", 1L);

        Reply reply2 = getReply(board, user);
        ReflectionTestUtils.setField(reply2, "id", 2L);

        Reply reply3 = getReply(board, user);
        ReflectionTestUtils.setField(reply3, "id", 3L);

        Reply reply4 = getReply(board, user);
        ReflectionTestUtils.setField(reply4, "id", 4L);

        Reply reply5 = getReply(board, user);
        ReflectionTestUtils.setField(reply5, "id", 5L);

        Reply reply6 = getReply(board, user);
        ReflectionTestUtils.setField(reply6, "id", 6L);

        Reply reply7 = getReply(board, user);
        ReflectionTestUtils.setField(reply7, "id", 7L);


        return Arrays.asList(
                reply1, reply2, reply3, reply4, reply5,
                reply6, reply7
        ).stream()
                .map(reply -> new ReplyDto.ReplyResponse(reply, null))
                .collect(Collectors.toList());

    }

    private List<ReplyDto.ReplyResponse> getRepliesByReply(Board board, User user) {

        Reply reply1 = getReply(board, user);
        ReflectionTestUtils.setField(reply1, "id", 1L);

        Reply reply2 = getReply(board, user);
        reply2.updateParentReply(reply1);
        ReflectionTestUtils.setField(reply2, "id", 2L);

        Reply reply3 = getReply(board, user);
        reply3.updateParentReply(reply1);
        ReflectionTestUtils.setField(reply3, "id", 3L);

        Reply reply4 = getReply(board, user);
        reply4.updateParentReply(reply3);
        ReflectionTestUtils.setField(reply4, "id", 4L);

        Reply reply5 = getReply(board, user);
        reply5.updateParentReply(reply3);
        ReflectionTestUtils.setField(reply5, "id", 5L);

        Reply reply6 = getReply(board, user);
        reply6.updateParentReply(reply2);
        ReflectionTestUtils.setField(reply6, "id", 6L);

        Reply reply7 = getReply(board, user);
        reply7.updateParentReply(reply6);
        ReflectionTestUtils.setField(reply7, "id", 7L);


        return Arrays.asList(
            reply1, reply2, reply3, reply4, reply5,
            reply6, reply7
        ).stream()
        .map(reply -> new ReplyDto.ReplyResponse(reply, null))
        .collect(Collectors.toList());

    }

    @DisplayName("댓글 추가 by 게시물 Service")
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

    @DisplayName("댓글 추가 by 댓글 Service")
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

    @DisplayName("댓글 리스트 조회 by 게시물 Service")
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

    @DisplayName("댓글 리스트 조회 by 댓글 Service")
    @Test
    public void 댓글_리스트_조회_by댓글_Service() throws Exception {

        //given
        int size = 10;
        int offset = 0;
        User user = getUser();
        Board board = getBoard(user);
        List<ReplyDto.ReplyResponse> repliesByReply = getRepliesByReply(board, user);

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

    @DisplayName("댓글 삭제 Service")
    @Test
    public void 댓글_삭제_Service() throws Exception {

        //given
        User user = getUser();
        Board board = getBoard(user);
        Reply reply = getReply(board, user);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(reply)).when(replyRepository).findById(any(Long.class), any(Long.class));
        doReturn(1L).when(replyRepository).delete(any(Reply.class));
        Long resValue = replyService.deleteReply(user.getId(), reply.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(replyRepository, times(1)).findById(any(Long.class), any(Long.class));
        verify(replyRepository, times(1)).delete(any(Reply.class));

    }
}
