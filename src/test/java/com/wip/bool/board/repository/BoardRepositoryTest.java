package com.wip.bool.board.repository;

import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.BoardRepository;
import com.wip.bool.board.domain.BoardType;
import com.wip.bool.board.dto.BoardDto;
import com.wip.bool.cmmn.board.BoardFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
@Transactional
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void init() throws Exception {
        User user = UserFactory.getNormalUser();
        userRepository.save(user);
    }

    @DisplayName("게시물 추가")
    @ParameterizedTest
    @EnumSource(BoardType.class)
    public void 게시물_추가_Repository(BoardType boardType) throws Exception {

        //given
        List<User> users = userRepository.findAll();
        User user = users.get(0);
        String title = "테스트 공지사항입니다.";
        String content = "공지사항 테스트";
        Board board = Board.createBoard(title, content, boardType, user);

        //when
        Board addBoard = boardRepository.save(board);

        //then
        assertThat(addBoard.getId()).isGreaterThan(0L);
        assertThat(addBoard.getBoardType()).isEqualTo(boardType);
        assertThat(addBoard.getTitle()).isEqualTo(title);
        assertThat(addBoard.getContent()).isEqualTo(content);
    }

    @DisplayName("게시물 리스트 조회")
    @Test
    public void 게시물_리스트_조회_Repository() throws Exception {

        //given
        List<User> users = userRepository.findAll();
        User user = users.get(0);
        int size = 10;
        int offset = 1;

        List<Board> boards = BoardFactory.getBoards(user);

        for(Board board : boards) {
            boardRepository.save(board);
        }

        //when
         List<BoardDto.BoardSimpleResponse> values = boardRepository.findAll(BoardType.BOARD, size, offset);

        //then
        assertThat(values.size()).isEqualTo(size-offset);
    }

    @DisplayName("게시물 상세 조회")
    @Test
    public void 게시물_상세_조회_Repository() throws Exception {

        //given
        String title = "테스트 공지사항입니다.";
        String content = "공지사항 테스트";

        List<User> users = userRepository.findAll();
        User user = users.get(0);
        Board addBoard = boardRepository.save(BoardFactory.getBoard(user, title, content));

        //when
        BoardDto.BoardResponse value = boardRepository.findDetailById(addBoard.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_BOARD));

        //then
        assertThat(value.getBoardId()).isEqualTo(addBoard.getId());
        assertThat(value.getTitle()).isEqualTo(title);
        assertThat(value.getContent()).isEqualTo(content);
        assertThat(value.getImages()).isNullOrEmpty();
    }

    @DisplayName("게시물 삭제")
    @Test
    public void 게시물_삭제_Repository() throws Exception {

        //given
        String title = "테스트 공지사항입니다.";
        String content = "공지사항 테스트";

        List<User> users = userRepository.findAll();
        User user = users.get(0);
        Board addBoard = boardRepository.save(BoardFactory.getBoard(user, title, content));

        //when
        Long resValue = boardRepository.delete(addBoard);

        //then
        assertThat(resValue).isEqualTo(1L);
    }
}
