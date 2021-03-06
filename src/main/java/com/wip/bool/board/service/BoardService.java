package com.wip.bool.board.service;

import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.BoardRepository;
import com.wip.bool.board.domain.BoardType;
import com.wip.bool.board.domain.ImageFile;
import com.wip.bool.board.dto.BoardDto;
import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    @Value("${spring.board.path}")
    private String boardFilePath;

    @Transactional
    public Long saveBoard(Long userId, BoardDto.BoardSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        BoardType boardType = BoardType.valueOf(requestDto.getBoardType());

        Board board = Board.createBoard(requestDto.getTitle(), requestDto.getContent(), boardType, user);

        Long id = boardRepository.save(board).getId();

        if(hasText(requestDto.getOrgFileNames())) {
            List<ImageFile> imageFiles = Arrays.stream(requestDto.getOrgFileNames().split(","))
                    .map(orgFileName -> ImageFile.createImageFile(boardFilePath, orgFileName))
                    .collect(Collectors.toList());

            updateBoard(board, imageFiles);

            if(!moveImageFile(imageFiles, requestDto.getTempFileNames())) {
                throw new IllegalStateException("?????? ????????? ?????????????????????.");
            }
        }

        return id;
    }


    @Transactional(readOnly = true)
    public List<BoardDto.BoardSimpleResponse> findBoards(String board, int size, int offset) {
        BoardType boardType = BoardType.valueOf(board);
        return boardRepository.findAll(boardType, size, offset);
    }

    @Transactional(readOnly = true)
    public BoardDto.BoardResponse findDetailBoard(Long boardId) {
        return boardRepository.findDetailById(boardId)
                .orElseThrow(() -> new EntityNotFoundException(boardId, ErrorCode.NOT_FOUND_BOARD));
    }

    @Transactional
    public Long deleteBoard(Long userId, Long boardId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role != Role.ROLE_ADMIN && role != Role.ROLE_NORMAL) {
            throw new AuthorizationException();
        }

        Board board = boardRepository.findById(userId, boardId, role)
                .orElseThrow(() -> new EntityNotFoundException(boardId, ErrorCode.NOT_FOUND_BOARD));

        boardRepository.delete(board);
        return 1L;
    }

    @Transactional
    public Long hiddenBoard(Long userId, Long boardId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role != Role.ROLE_ADMIN && role != Role.ROLE_NORMAL) {
            throw new AuthorizationException();
        }

        Board board = boardRepository.findById(userId, boardId, role)
                .orElseThrow(() -> new EntityNotFoundException(boardId, ErrorCode.NOT_FOUND_BOARD));

        board.hiddenStatus();
        return 1L;
    }

    private void updateBoard(Board board, List<ImageFile> imageFiles) {

        for(ImageFile imageFile : imageFiles) {
            imageFile.updateBoard(board);
        }
    }

    private boolean moveImageFile(List<ImageFile> imageFiles, String tempFileNames) {

        //?????? ??????
        int cnt = 0;
        boolean isSuccess = true;
        String[] tempFilePaths = tempFileNames.split(",");

        for(cnt=0;cnt<imageFiles.size();cnt++) {
            ImageFile imageFile = imageFiles.get(cnt);
            if(!imageFile.createImageFile(tempFilePaths[cnt])) {
                isSuccess = false;
                break;
            }
        }

        if(!isSuccess) {
            for(int i=0;i<cnt;i++) {
                imageFiles.get(i).deleteImageFile();
            }
        }

        return isSuccess;
    }
}
