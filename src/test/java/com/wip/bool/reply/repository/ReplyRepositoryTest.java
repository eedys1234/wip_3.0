package com.wip.bool.reply.repository;

import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.BoardRepository;
import com.wip.bool.board.domain.Reply;
import com.wip.bool.board.domain.ReplyRepository;
import com.wip.bool.board.dto.ReplyDto;
import com.wip.bool.cmmn.board.BoardFactory;
import com.wip.bool.cmmn.reply.ReplyFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private User getSaveUser() {
        User user = UserFactory.getNormalUser();
        return userRepository.save(user);
    }

    private Board getSaveBoard(User user) {
        Board board = BoardFactory.getBoard(user);
        boardRepository.save(board);
        return board;
    }

    private Reply getSaveReply(Board board, User user) {
        Reply reply = ReplyFactory.getReply(board, user);
        return reply;
    }

    @DisplayName("댓글 추가 by 게시물 Repository")
    @Test
    public void 댓글_추가_by게시물_Repository() throws Exception {

        //given
        User user = getSaveUser();
        Board board = getSaveBoard(user);
        Reply reply = getSaveReply(board, user);

        //when
        Reply addReply = replyRepository.save(reply);

        //then
        assertThat(addReply.getId()).isEqualTo(reply.getId());
        assertThat(addReply.getContent()).isEqualTo("테스트 댓글");
        assertThat(addReply.getParentReply()).isNull();
        assertThat(addReply.getChildReply()).isEmpty();
    }

    @DisplayName("댓글 추가 by 댓글 Repository")
    @Test
    public void 댓글_추가_by댓글_Repository() throws Exception {

        //given
        User user = getSaveUser();
        Board board = getSaveBoard(user);
        Reply parentReply = getSaveReply(board, user);
        Reply addParentReply = replyRepository.save(parentReply);
        Reply childReply = getSaveReply(board, user);
        childReply.updateParentReply(addParentReply);

        //when
        Reply addChildReply = replyRepository.save(childReply);

        //then
        assertThat(addChildReply.getId()).isEqualTo(childReply.getId());
        assertThat(addChildReply.getParentReply().getId()).isEqualTo(parentReply.getId());
    }

    @DisplayName("댓글 리스트 조회 by 게시물 Repository")
    @Test
    public void 댓글_리스트_조회_by게시물_Repository() throws Exception {

        //given
        User user = getSaveUser();
        Board board = getSaveBoard(user);
        List<Reply> replies = new ArrayList<>();
        int size = 10;
        int offset = 0;
        int cnt = 10;

        for(int i=0;i<cnt;i++) {
            Reply reply = getSaveReply(board, user);
            replyRepository.save(reply);
            replies.add(reply);
        }

        //when
        List<ReplyDto.ReplyResponse> values = replyRepository.findAllByBoard(board.getId(), size, offset);

        //then
        assertThat(values.size()).isEqualTo(cnt);
        assertThat(values).extracting(ReplyDto.ReplyResponse::getReplyId)
                .containsAll(replies.stream()
                        .map(Reply::getId)
                        .collect(Collectors.toList()));
    }

    @DisplayName("댓글 리스트 조회 by 댓글 Repository")
    @Test
    public void 댓글_리스트_조회_by댓글_Repository() throws Exception {

        //given
        int size = 10;
        int offset = 0;
        int cnt = 10;
        User user = getSaveUser();
        Board board = getSaveBoard(user);
        Reply parentReply = getSaveReply(board, user);
        Reply addParentReply = replyRepository.save(parentReply);
        List<Long> childReplyIds = new ArrayList<>();

        for(int i=0;i<cnt;i++)
        {
            Reply childReply = getSaveReply(board, user);
            childReply.updateParentReply(addParentReply);
            Reply addChildReply = replyRepository.save(childReply);
            childReplyIds.add(addChildReply.getId());
        }

        //when
        List<ReplyDto.ReplyResponse> replies = replyRepository.findAllByReply(addParentReply.getId(), size, offset);

        //then
        assertThat(replies.size()).isEqualTo(cnt);
        assertThat(replies).extracting(ReplyDto.ReplyResponse::getParentId).contains(addParentReply.getId());
        assertThat(replies).extracting(ReplyDto.ReplyResponse::getReplyId).containsAll(childReplyIds);
    }

    @DisplayName("댓글 삭제 Repository")
    @Test
    public void 댓글_삭제_Repository() throws Exception {

        //given
        User user = getSaveUser();
        Board board = getSaveBoard(user);
        Reply reply = getSaveReply(board, user);

        Reply addReply = replyRepository.save(reply);

        //when
        Long resValue = replyRepository.delete(addReply);

        //then
        assertThat(resValue).isEqualTo(1L);

    }
}
