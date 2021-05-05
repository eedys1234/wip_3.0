package com.wip.bool.board.service;

import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.BoardRepository;
import com.wip.bool.board.domain.BoardType;
import com.wip.bool.board.domain.ImageFile;
import com.wip.bool.board.dto.BoardDto;
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
    public Long save(Long userId, BoardDto.BoardSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다. id = " + userId));

        BoardType boardType = BoardType.valueOf(requestDto.getBoardType());

        Board board = Board.createBoard(requestDto.getTitle(), requestDto.getContent(), boardType, user);

        Long id = boardRepository.save(board).getId();

        if(hasText(requestDto.getOrgFileNames())) {
            List<ImageFile> imageFiles = Arrays.stream(requestDto.getOrgFileNames().split(","))
                    .map(orgFileName -> ImageFile.createImageFile(boardFilePath, orgFileName))
                    .collect(Collectors.toList());

            board.updateImageFiles(imageFiles);

            if(!moveImageFile(imageFiles, requestDto.getTempFileNames())) {
                throw new IllegalStateException("파일 저장이 실패하였습니다.");
            }
        }

        return id;
    }

    @Transactional(readOnly = true)
    public List<BoardDto.BoardSimpleResponse> gets(String board, int size, int offset) {
        BoardType boardType = BoardType.valueOf(board);
        return boardRepository.findAll(boardType, size, offset);
    }

    @Transactional(readOnly = true)
    public BoardDto.BoardResponse get(Long boardId) {
        List<BoardDto.BoardResponse> boards = boardRepository.findDetailById(boardId);
        return boards.get(0);
    }

    @Transactional
    public Long delete(Long userId, Long boardId) {
        Board board = boardRepository.findById(userId, boardId)
                                    .orElseThrow(() -> new IllegalArgumentException());

        board.deleteStatus();
        return 1L;
    }

    private boolean moveImageFile(List<ImageFile> imageFiles, String tempFileNames) {

        //파일 생성
        int cnt = 0;
        boolean isSuccess = true;
        String[] tempFilePaths = tempFileNames.split(",");

        for(cnt=0;cnt<imageFiles.size();cnt++) {
            if(!imageFiles.get(cnt).createImageFile(tempFilePaths[cnt])) {
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
