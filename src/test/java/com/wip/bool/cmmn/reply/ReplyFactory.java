package com.wip.bool.cmmn.reply;

import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.Reply;
import com.wip.bool.board.dto.ReplyDto;
import com.wip.bool.user.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReplyFactory {

    public static List<Reply> getReplies(Board board, User user) {
        return IntStream.rangeClosed(1, 10)
            .mapToObj(num -> getReply(board, user))
            .collect(Collectors.toList());
    }

    public static List<Reply> getRepliesWithId(Board board, User user) {
        AtomicInteger index = new AtomicInteger(0);
        return IntStream.rangeClosed(1, 10)
                .mapToObj(num -> getReply(board, user, index.incrementAndGet()))
                .collect(Collectors.toList());
    }

    public static Reply getReply(Board board, User user) {
        String content = "테스트 댓글";
        Reply reply = Reply.createReply(content, board, user);
        return reply;
    }

    public static Reply getReply(Board board, User user, long id) {
        String content = "테스트 댓글";
        Reply reply = Reply.createReply(content, board, user);
        ReflectionTestUtils.setField(reply, "id", id);
        return reply;
    }

    public static List<ReplyDto.ReplyResponse> getRepliesByReplies(Board board, User user) {

          List<Reply> replies = getRepliesWithId(board, user);

          replies.get(1).updateParentReply(replies.get(0));
          replies.get(2).updateParentReply(replies.get(0));
          replies.get(3).updateParentReply(replies.get(2));
          replies.get(4).updateParentReply(replies.get(2));
          replies.get(5).updateParentReply(replies.get(1));
          replies.get(6).updateParentReply(replies.get(5));
          replies.get(7).updateParentReply(replies.get(6));
          replies.get(8).updateParentReply(replies.get(6));
          replies.get(9).updateParentReply(replies.get(6));

          return replies.stream()
                  .map(reply -> new ReplyDto.ReplyResponse(reply, null))
                  .collect(Collectors.toList());

    }
}
