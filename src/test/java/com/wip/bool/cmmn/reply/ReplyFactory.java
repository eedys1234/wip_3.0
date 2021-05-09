package com.wip.bool.cmmn.reply;

import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.Reply;
import com.wip.bool.board.dto.ReplyDto;
import com.wip.bool.user.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReplyFactory {

    public static Reply getReply(Board board, User user) {
        String content = "테스트 댓글";
        Reply reply = Reply.createReply(content, board, user);
        return reply;
    }

    public static List<ReplyDto.ReplyResponse> getRepliesByBoard(Board board, User user) {

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

    public static List<ReplyDto.ReplyResponse> getRepliesByReplies(Board board, User user) {

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
}
