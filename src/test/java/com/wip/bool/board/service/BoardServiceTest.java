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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BoardRepository boardRepository;

    @DisplayName("게시물 추가")
    @Test
    public void 게시물_추가_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        Board board = BoardFactory.getNotice(user, 1L);

        BoardDto.BoardSaveRequest requestDto = new BoardDto.BoardSaveRequest();
        ReflectionTestUtils.setField(requestDto, "title", "테스트 게시물");
        ReflectionTestUtils.setField(requestDto, "content", "게시물 내용");
        ReflectionTestUtils.setField(requestDto, "boardType", "NOTICE");
//        ReflectionTestUtils.setField(requestDto, "orgFileNames", "Test.png,Test2.png");
//        ReflectionTestUtils.setField(requestDto, "tempFileNames", "12345,12346");

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());

        doReturn(board).when(boardRepository).save(any(Board.class));
        Long resValue = boardService.saveBoard(1L, requestDto);

        //then
        assertThat(resValue).isEqualTo(board.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @DisplayName("게시물_삭제_소유자")
    @Test
    public void 게시물_삭제_소유자_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        ReflectionTestUtils.setField(user, "role", Role.ROLE_NORMAL);
        Board board = BoardFactory.getNotice(user, 1L);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(board)).when(boardRepository).findById(anyLong(), anyLong());
        Long resValue = boardService.deleteBoard(1L, 1L);

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(boardRepository, times(1)).findById(anyLong(), anyLong());

    }
    @DisplayName("게시물_삭제_관리자")
    @Test
    public void 게시물_삭제_관리자_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        ReflectionTestUtils.setField(user, "role", Role.ROLE_ADMIN);
        Board board = BoardFactory.getNotice(user, 1L);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(board)).when(boardRepository).findById(anyLong());
        Long resValue = boardService.deleteBoard(1L, 1L);

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(boardRepository, times(1)).findById(anyLong());

    }

    @DisplayName("게시물_리스트_조회")
    @Test
    public void 게시물_리스트_조회_Service() throws Exception {

        //given
        int size = 10;
        int offset = 0;
        String boardType = "NOTICE";
        User user = UserFactory.getNormalUser(1L);
        List<Board> boards = BoardFactory.getBoardsWithId(user);

        //when
        doReturn(boards.stream()
        .map(board -> new BoardDto.BoardSimpleResponse(board.getId(), board.getTitle(), board.getIsDeleted(), board.getBoardType()))
        .collect(Collectors.toList())).when(boardRepository).findAll(any(BoardType.class), anyInt(), anyInt());

        List<BoardDto.BoardSimpleResponse> values = boardService.findBoards(boardType, size, offset);

        //then
        assertThat(values.size()).isEqualTo(boards.size());
        assertThat(values).extracting(BoardDto.BoardSimpleResponse::getBoardId)
        .containsAll(boards.stream().map(Board::getId).collect(Collectors.toList()));

        //verify
        verify(boardRepository, times(1)).findAll(any(BoardType.class), anyInt(), anyInt());
    }

    @DisplayName("게시물_상세조회")
    @Test
    public void 게시물_상세조회_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        Board board = BoardFactory.getNotice(user, 1L);
        BoardDto.BoardResponse responseDto = new BoardDto.BoardResponse(board);

        //when
        doReturn(Optional.ofNullable(responseDto)).when(boardRepository).findDetailById(anyLong());
        BoardDto.BoardResponse value = boardService.findDetailBoard(board.getId());

        //then
        assertThat(value.getBoardId()).isEqualTo(board.getId());
        assertThat(value.getImages()).isNullOrEmpty();

        //verify
        verify(boardRepository, times(1)).findDetailById(anyLong());
    }

    @DisplayName("게시물_숨김처리_소유자")
    @Test
    public void 게시물_숨김처리_소유자_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        ReflectionTestUtils.setField(user, "role", Role.ROLE_NORMAL);
        Board board = BoardFactory.getNotice(user, 1L);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(board)).when(boardRepository).findById(anyLong(), anyLong());
        Long resValue = boardService.hiddenBoard(user.getId(), board.getId());

        //then
        assertThat(resValue).isEqualTo(1L);
        assertThat(board.getIsDeleted()).isEqualTo(DeleteStatus.HIDDEN);

        //verify
        verify(userRepository, timeout(1)).findById(anyLong());
        verify(boardRepository, times(1)).findById(anyLong(), anyLong());
    }


    @DisplayName("게시물_숨김처리_관리자")
    @Test
    public void 게시물_숨김처리_관리자_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        ReflectionTestUtils.setField(user, "role", Role.ROLE_ADMIN);
        Board board = BoardFactory.getNotice(user, 1L);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(board)).when(boardRepository).findById(anyLong());
        Long resValue = boardService.hiddenBoard(user.getId(), board.getId());

        //then
        assertThat(resValue).isEqualTo(1L);
        assertThat(board.getIsDeleted()).isEqualTo(DeleteStatus.HIDDEN);

        //verify
        verify(userRepository, timeout(1)).findById(anyLong());
        verify(boardRepository, times(1)).findById(anyLong());
    }
    
}
