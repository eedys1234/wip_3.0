package com.wip.bool.cmmn.reply;

import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.Reply;
import com.wip.bool.user.domain.User;

public class ReplyFactory {

    public static Reply getReply(Board board, User user) {
        String content = "테스트 댓글";
        Reply reply = Reply.createReply(content, board, user);
        return reply;
    }
}
