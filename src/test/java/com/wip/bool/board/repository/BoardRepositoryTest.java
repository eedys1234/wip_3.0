package com.wip.bool.board.repository;

import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.BoardRepository;
import com.wip.bool.board.domain.BoardType;
import com.wip.bool.board.dto.BoardDto;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.user.domain.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

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

        String email = "test@gmail.com";
        String password = "test1234";
        UserType userType = UserType.WIP;
        String profile = "";
        Role role = Role.ROLE_NORMAL;
        User user = User.createUser(email, password, profile, userType, role);

        userRepository.save(user);
    }

    private static Stream<Arguments> boardListParameters() {
        return Stream.of(
                Arguments.of("게시글입니다1", "내용1", BoardType.BOARD),
                Arguments.of("게시글입니다2", "내용2", BoardType.BOARD),
                Arguments.of("게시글입니다3", "내용3", BoardType.BOARD),
                Arguments.of("게시글입니다4", "내용4", BoardType.BOARD),
                Arguments.of("게시글입니다5", "내용5", BoardType.BOARD),
                Arguments.of("게시글입니다6", "내용6", BoardType.BOARD),
                Arguments.of("게시글입니다7", "내용7", BoardType.BOARD),
                Arguments.of("게시글입니다8", "내용8", BoardType.BOARD),
                Arguments.of("게시글입니다9", "내용9", BoardType.BOARD),
                Arguments.of("게시글입니다10", "내용10", BoardType.NOTICE),
                Arguments.of("게시글입니다11", "내용11", BoardType.BOARD),
                Arguments.of("게시글입니다12", "내용12", BoardType.NOTICE)
        );
    }

    @DisplayName("게시물 추가")
    @ParameterizedTest
    @EnumSource(BoardType.class)
    public void 게시물_추가(BoardType boardType) throws Exception {

        //given
        String title = "테스트 공지사항입니다.";
        String content = "공지사항 테스트";

        List<User> users = userRepository.findAll();
        Board board = Board.createBoard(title, content, boardType, users.get(0));

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
    public void 게시물_리스트_조회() throws Exception {

        //given
        List<User> users = userRepository.findAll();

        for(int i=0;i<12;i++) {
            String title = "게시글입니다" + i;
            String content = "내용" + i;
            BoardType boardType = BoardType.BOARD;

            Board board = Board.createBoard(title, content, boardType, users.get(0));
            boardRepository.save(board);
        }

        //when
         List<BoardDto.BoardSimpleResponse> boards = boardRepository.findAll(BoardType.BOARD, 10, 10);

        //then
        assertThat(boards.size()).isEqualTo(2);
    }

    @DisplayName("게시물 상세 조회")
    @Test
    public void 게시물_상세_조회() throws Exception {

        //given
        String title = "테스트 공지사항입니다.";
        String content = "공지사항 테스트";

        List<User> users = userRepository.findAll();
        Board board = Board.createBoard(title, content, BoardType.BOARD, users.get(0));
        Board addBoard = boardRepository.save(board);

        //when
        BoardDto.BoardResponse dto = boardRepository.findDetailById(addBoard.getId()).get(0);

        //then
        assertThat(dto.getBoardId()).isEqualTo(addBoard.getId());
        assertThat(dto.getImages()).isEqualTo(null);
    }

    @DisplayName("게시물 삭제")
    @ParameterizedTest
    @EnumSource(BoardType.class)
    public void 게시물_삭제(BoardType boardType) throws Exception {

        //given
        String title = "테스트 공지사항입니다.";
        String content = "공지사항 테스트";

        List<User> users = userRepository.findAll();
        Board board = Board.createBoard(title, content, boardType, users.get(0));
        Board addBoard = boardRepository.save(board);

        //when
        Long resValue = boardRepository.delete(addBoard);

        //then
        assertThat(resValue).isEqualTo(1L);
    }
}
