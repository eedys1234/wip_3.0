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

    @DisplayName("댓글 추가 by 게시물")
    @Test
    public void 댓글_추가_by게시물_Repository() throws Exception {

        //given
        User user = UserFactory.getNormalUser();
        userRepository.save(user);

        Board board = BoardFactory.getBoard(user);
        boardRepository.save(board);

        Reply reply = ReplyFactory.getReply(board, user);

        //when
        Reply addReply = replyRepository.save(reply);

        //then
        assertThat(addReply.getId()).isGreaterThan(0L);
        assertThat(addReply.getId()).isEqualTo(reply.getId());
        assertThat(addReply.getContent()).isEqualTo(reply.getContent());
        assertThat(addReply.getParentReply()).isNull();
        assertThat(addReply.getChildReply()).isEmpty();
    }

    @DisplayName("댓글 추가 by 댓글")
    @Test
    public void 댓글_추가_by댓글_Repository() throws Exception {

        //given
        User user = UserFactory.getNormalUser();
        userRepository.save(user);

        Board board = BoardFactory.getBoard(user);
        boardRepository.save(board);

        Reply parentReply = ReplyFactory.getReply(board, user);
        Reply addParentReply = replyRepository.save(parentReply);

        Reply childReply = ReplyFactory.getReply(board, user);
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
        int size = 10;
        int offset = 0;

        User user = UserFactory.getNormalUser();
        User addUser = userRepository.save(user);

        Board board = BoardFactory.getBoard(user);
        boardRepository.save(board);

        List<Reply> replies = ReplyFactory.getReplies(board, addUser);

        for(Reply reply : replies) {
            replyRepository.save(reply);
        }

        //when
        List<ReplyDto.ReplyResponse> values = replyRepository.findAllByBoard(board.getId(), size, offset);

        //then
        assertThat(values.size()).isEqualTo(replies.size());
        assertThat(values).extracting(ReplyDto.ReplyResponse::getReplyId).containsAll(replies.stream()
                                                                                            .map(Reply::getId)
                                                                                            .collect(Collectors.toList()));

        assertThat(values).extracting(ReplyDto.ReplyResponse::getContent).containsAll(replies.stream()
                                                                                            .map(Reply::getContent)
                                                                                            .collect(Collectors.toList()));
    }

    @DisplayName("댓글 리스트 조회 by 댓글")
    @Test
    public void 댓글_리스트_조회_by댓글_Repository() throws Exception {

        //given
        int size = 10;
        int offset = 0;

        User user = UserFactory.getNormalUser();
        User addUser = userRepository.save(user);

        Board board = BoardFactory.getBoard(user);
        boardRepository.save(board);

        Reply parentReply = ReplyFactory.getReply(board, user);
        Reply addParentReply = replyRepository.save(parentReply);

        List<Reply> replies = ReplyFactory.getReplies(board, addUser);

        for(Reply reply : replies)
        {
            reply.updateParentReply(addParentReply);
            replyRepository.save(reply);
        }

        //when
        List<ReplyDto.ReplyResponse> values = replyRepository.findAllByReply(addParentReply.getId(), size, offset);

        //then
        assertThat(values.size()).isEqualTo(replies.size());
        assertThat(values).extracting(ReplyDto.ReplyResponse::getParentId).contains(addParentReply.getId());
        assertThat(values).extracting(ReplyDto.ReplyResponse::getReplyId).containsAll(replies.stream().map(reply -> reply.getId()).collect(Collectors.toList()));
    }

    @DisplayName("댓글 삭제")
    @Test
    public void 댓글_삭제_Repository() throws Exception {

        //given
        User user = UserFactory.getNormalUser();
        userRepository.save(user);

        Board board = BoardFactory.getBoard(user);
        boardRepository.save(board);

        Reply reply = ReplyFactory.getReply(board, user);
        Reply addReply = replyRepository.save(reply);

        //when
        Long resValue = replyRepository.delete(addReply);

        //then
        assertThat(resValue).isEqualTo(1L);
    }
}
