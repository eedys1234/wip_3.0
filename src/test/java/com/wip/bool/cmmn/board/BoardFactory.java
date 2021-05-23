package com.wip.bool.cmmn.board;

import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.BoardType;
import com.wip.bool.user.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BoardFactory {

    public static String[] boardTitles = {
        "게시글_1",
        "게시글_2",
        "게시글_3",
        "게시글_4",
        "게시글_5",
        "게시글_6",
        "게시글_7",
        "게시글_8",
        "게시글_9",
        "게시글_10",
    };

    public static String[] boardContents = {
        "내용_1",
        "내용_2",
        "내용_3",
        "내용_4",
        "내용_5",
        "내용_6",
        "내용_7",
        "내용_8",
        "내용_9",
        "내용_10",
    };

    public static List<Board> getBoards(User user) {
        AtomicInteger index = new AtomicInteger(0);
        return Arrays.stream(boardTitles)
                .map(title -> {
                    Board board = getBoard(user, title, boardContents[index.intValue()]);
                    index.incrementAndGet();
                    return board;
                })
                .collect(Collectors.toList());
    }

    public static List<Board> getBoardsWithId(User user) {
        AtomicInteger index = new AtomicInteger(0);
        List<Board> boards = Arrays.stream(boardTitles)
                .map(title -> {
                    Board board = getBoard(user, title, boardContents[index.intValue()], index.longValue());
                    index.incrementAndGet();
                    return board;
                })
                .collect(Collectors.toList());

        boards.get(0).deleteStatus();
        return boards;
    }

    public static Board getBoard(User user) {

        String title = "테스트 게시물";
        String content = "게시물 내용";
        Board board = getBoard(user, title, content);
        return board;
    }

    public static Board getBoard(User user, long id) {

        String title = "테스트 게시물";
        String content = "게시물 내용";
        Board board = getBoard(user, title, content);
        ReflectionTestUtils.setField(board, "id", id);
        return board;
    }

    public static Board getBoard(User user, String title, String content) {

        BoardType boardType = BoardType.BOARD;
        Board board = Board.createBoard(title, content, boardType, user);
        return board;
    }


    public static Board getBoard(User user, String title, String content, long id) {

        Board board = getBoard(user, title, content);
        ReflectionTestUtils.setField(board, "id", id);
        return board;
    }

    public static Board getNotice(User user) {

        String title = "테스트 게시물";
        String content = "게시물 내용";
        Board board = getNotice(user, title, content);
        return board;
    }

    public static Board getNotice(User user, long id) {

        String title = "테스트 게시물";
        String content = "게시물 내용";
        Board board = getNotice(user, title, content);
        ReflectionTestUtils.setField(board, "id", id);
        return board;
    }

    public static Board getNotice(User user, String title, String content) {

        BoardType boardType = BoardType.NOTICE;
        Board board = Board.createBoard(title, content, boardType, user);
        return board;
    }

    public static Board getNotice(User user, String title, String content, long id) {

        Board board = getNotice(user, title, content);
        ReflectionTestUtils.setField(board, "id", id);
        return board;
    }

}
