package com.wip.bool.reply.repository;

import com.wip.bool.board.domain.*;
import com.wip.bool.board.dto.ReplyDto;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.user.domain.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles("test")
@Transactional
public class ReplyRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    private User getUser() {

        String email = "test@gmail.com";
        String password = "test1234";
        String profiles = "";
        UserType userType = UserType.WIP;
        Role role = Role.ROLE_NORMAL;

        User user = User.createUser(email, password, profiles, userType, role);
        userRepository.save(user);
        return user;
    }

    private Board getBoard(User user) {

        String title = "테스트 게시물";
        String content = "게시물 내용";
        BoardType boardType = BoardType.BOARD;

        Board board = Board.createBoard(title, content, boardType, user);
        boardRepository.save(board);
        return board;
    }

    private Reply getReply(Board board, User user) {
        String content = "테스트 댓글";
        Reply reply = Reply.createReply(content, board, user);
        return reply;
    }

    @DisplayName("댓글 추가 by 게시물")
    @Test
    public void 댓글_추가_by게시물_Repository() throws Exception {

        //given
        User user = getUser();
        Board board = getBoard(user);
        Reply reply = getReply(board, user);

        //when
        Reply addReply = replyRepository.save(reply);

        //then
        assertThat(addReply.getId()).isEqualTo(reply.getId());
        assertThat(addReply.getContent()).isEqualTo("테스트 댓글");
        assertThat(addReply.getParentReply()).isNull();
        assertThat(addReply.getChildReply()).isEmpty();
    }

    @DisplayName("댓글 추가 by 댓글")
    @Test
    public void 댓글_추가_by댓글_Repository() throws Exception {

        //given
        User user = getUser();
        Board board = getBoard(user);
        Reply parentReply = getReply(board, user);
        Reply addParentReply = replyRepository.save(parentReply);
        Reply childReply = getReply(board, user);
        childReply.updateParentReply(addParentReply);

        //when
        Reply addChildReply = replyRepository.save(childReply);

        //then
        assertThat(addChildReply.getId()).isEqualTo(childReply.getId());
        assertThat(addChildReply.getParentReply().getId()).isEqualTo(parentReply.getId());
    }

    @DisplayName("댓글 리스트 조회 by 게시물")
    @Test
    public void 댓글_리스트_조회_by게시물_Repository() throws Exception {

        //given
        User user = getUser();
        Board board = getBoard(user);
        int size = 10;
        int offset = 0;
        int cnt = 10;

        for(int i=0;i<cnt;i++) {
            Reply reply = getReply(board, user);
            replyRepository.save(reply);
        }

        //when
        List<ReplyDto.ReplyResponse> replies = replyRepository.findAllByBoard(board.getId(), size, offset);

        //then
        assertThat(replies.size()).isEqualTo(cnt);
        assertThat(replies).extracting(ReplyDto.ReplyResponse::getReplyId)
                .contains(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
    }

//    @DisplayName("")
//    @Test
//    public void 댓글_리스트_조회
}
