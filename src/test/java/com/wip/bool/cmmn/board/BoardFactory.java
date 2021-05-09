package com.wip.bool.cmmn.board;

import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.BoardType;
import com.wip.bool.user.domain.User;

public class BoardFactory {

    public static Board getBoard(User user) {

        String title = "테스트 게시물";
        String content = "게시물 내용";
        BoardType boardType = BoardType.BOARD;
        Board board = Board.createBoard(title, content, boardType, user);
        return board;
    }
}
