package com.wip.bool.board.service;

import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.BoardRepository;
import com.wip.bool.board.domain.BoardType;
import com.wip.bool.board.dto.BoardDto;
import com.wip.bool.cmmn.board.BoardFactory;
import com.wip.bool.cmmn.status.DeleteStatus;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BoardRepository boardRepository;

    private User getUser() {
        User user = UserFactory.getNormalUser();
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private Board getBoard(User user) {

        Board board = BoardFactory.getNotice(user);
        ReflectionTestUtils.setField(board, "id", 1L);
        return board;
    }

    private List<BoardDto.BoardSimpleResponse> getBoards(User user) {

        Board board1 = getBoard(user);
        Board board2 = getBoard(user);
        Board board3 = getBoard(user);
        Board board4 = getBoard(user);
        Board board5 = getBoard(user);
        Board board6 = getBoard(user);
        Board board7 = getBoard(user);
        Board board8 = getBoard(user);
        Board board9 = getBoard(user);
        Board board10 = getBoard(user);

        ReflectionTestUtils.setField(board1, "id", 1L);
        ReflectionTestUtils.setField(board2, "id", 2L);
        ReflectionTestUtils.setField(board3, "id", 3L);
        ReflectionTestUtils.setField(board4, "id", 4L);
        ReflectionTestUtils.setField(board5, "id", 5L);
        ReflectionTestUtils.setField(board6, "id", 6L);
        ReflectionTestUtils.setField(board7, "id", 7L);
        ReflectionTestUtils.setField(board8, "id", 8L);
        ReflectionTestUtils.setField(board9, "id", 9L);
        ReflectionTestUtils.setField(board10, "id", 10L);

        List<BoardDto.BoardSimpleResponse> boards = Arrays.asList(board1, board2,
                board3, board4, board5, board6, board7, board8,
                board9, board10)
                .stream()
                .map(board -> new BoardDto.BoardSimpleResponse(board.getId(),
                        board.getTitle(), board.getIsDeleted(), board.getBoardType()))
                .collect(Collectors.toList());

        return boards;
    }

    @DisplayName("게시물 추가")
    @Test
    public void 게시물_추가_Service() throws Exception {

        //given
        User user = getUser();
        Board board = getBoard(user);

        BoardDto.BoardSaveRequest requestDto = new BoardDto.BoardSaveRequest();
        ReflectionTestUtils.setField(requestDto, "title", "테스트 게시물");
        ReflectionTestUtils.setField(requestDto, "content", "게시물 내용");
        ReflectionTestUtils.setField(requestDto, "boardType", "NOTICE");
//        ReflectionTestUtils.setField(requestDto, "orgFileNames", "Test.png,Test2.png");
//        ReflectionTestUtils.setField(requestDto, "tempFileNames", "12345,12346");

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));

        doReturn(board).when(boardRepository).save(any(Board.class));
        Long resValue = boardService.saveBoard(1L, requestDto);

        //then
        assertThat(resValue).isEqualTo(board.getId());

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @DisplayName("게시물_삭제_소유자")
    @Test
    public void 게시물_삭제_소유자_Service() throws Exception {

        //given
        User user = getUser();
        ReflectionTestUtils.setField(user, "role", Role.ROLE_NORMAL);
        Board board = getBoard(user);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(board)).when(boardRepository).findById(any(Long.class), any(Long.class));
        Long resValue = boardService.deleteBoard(1L, 1L);

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(boardRepository, times(1)).findById(any(Long.class), any(Long.class));

    }
    @DisplayName("게시물_삭제_관리자")
    @Test
    public void 게시물_삭제_관리자_Service() throws Exception {

        //given
        User user = getUser();
        ReflectionTestUtils.setField(user, "role", Role.ROLE_ADMIN);
        Board board = getBoard(user);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(board)).when(boardRepository).findById(any(Long.class));
        Long resValue = boardService.deleteBoard(1L, 1L);

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(boardRepository, times(1)).findById(any(Long.class));

    }

    @DisplayName("게시물_리스트_조회")
    @Test
    public void 게시물_리스트_조회_Service() throws Exception {

        //given
        User user = getUser();
        List<BoardDto.BoardSimpleResponse> boards = getBoards(user);

        //when
        doReturn(boards).when(boardRepository).findAll(any(BoardType.class), any(Integer.class), any(Integer.class));
        List<BoardDto.BoardSimpleResponse> values = boardService.findBoards("NOTICE", 10, 0);

        //then
        assertThat(values.size()).isEqualTo(10);
        assertThat(values).extracting(BoardDto.BoardSimpleResponse::getTitle)
        .contains(boards.get(0).getTitle());

        //verify
        verify(boardRepository, times(1)).findAll(any(BoardType.class), any(Integer.class), any(Integer.class));
    }

    @DisplayName("게시물_상세조회")
    @Test
    public void 게시물_상세조회_Service() throws Exception {

        //given
        User user = getUser();
        BoardDto.BoardResponse requestDto = new BoardDto.BoardResponse(getBoard(user));

        //when
        doReturn(requestDto).when(boardRepository).findDetailById(any(Long.class));
        BoardDto.BoardResponse boardResponse = boardService.findDetailBoard(1L);

        //then
        assertThat(boardResponse.getBoardId()).isEqualTo(1L);
        assertThat(boardResponse.getImages()).isNullOrEmpty();

        //verify
        verify(boardRepository, times(1)).findDetailById(any(Long.class));
    }

    @DisplayName("게시물_숨김처리_소유자")
    @Test
    public void 게시물_숨김처리_소유자_Service() throws Exception {

        //given
        User user = getUser();
        ReflectionTestUtils.setField(user, "role", Role.ROLE_NORMAL);
        Board board = getBoard(user);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(board)).when(boardRepository).findById(any(Long.class), any(Long.class));
        Long resValue = boardService.hiddenBoard(1L, 1L);

        //then
        assertThat(resValue).isEqualTo(1L);
        assertThat(board.getIsDeleted()).isEqualTo(DeleteStatus.HIDDEN);

        //verify
        verify(userRepository, timeout(1)).findById(any(Long.class));
        verify(boardRepository, times(1)).findById(any(Long.class), any(Long.class));
    }


    @DisplayName("게시물_숨김처리_관리자")
    @Test
    public void 게시물_숨김처리_관리자_Service() throws Exception {

        //given
        User user = getUser();
        ReflectionTestUtils.setField(user, "role", Role.ROLE_ADMIN);
        Board board = getBoard(user);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(board)).when(boardRepository).findById(any(Long.class));
        Long resValue = boardService.hiddenBoard(1L, 1L);

        //then
        assertThat(resValue).isEqualTo(1L);
        assertThat(board.getIsDeleted()).isEqualTo(DeleteStatus.HIDDEN);

        //verify
        verify(userRepository, timeout(1)).findById(any(Long.class));
        verify(boardRepository, times(1)).findById(any(Long.class));
    }
    
}
