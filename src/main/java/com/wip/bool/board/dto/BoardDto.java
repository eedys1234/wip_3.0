package com.wip.bool.board.dto;

import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.BoardType;
import com.wip.bool.board.domain.ImageFile;
import com.wip.bool.cmmn.status.DeleteStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

public class BoardDto {

    @Getter
    @NoArgsConstructor
    public static class BoardSaveRequest {

        @NotBlank
        private String title;

        @NotBlank
        private String content;

        @NotBlank
        private String boardType;

        private String orgFileNames;

        private String tempFileNames;

    }

    @Getter
    @NoArgsConstructor
    public static class BoardSimpleResponse {

        private Long boardId;

        private String title;

        private BoardType boardType;

        public BoardSimpleResponse(Long boardId, String title, DeleteStatus isDeleted, BoardType boardType) {
            this.boardId = boardId;
            this.title = isDeleted == DeleteStatus.DELETE ? "삭제된 게시글입니다." : title;
            this.boardType = boardType;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class BoardResponse {

        private Long boardId;

        private String title;

        private String content;

        private BoardType boardType;

        private List<ImageFileDto.ImageFileResponse> images;

        public BoardResponse(Board board, List<ImageFile> imageFiles) {
            this.boardId = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.boardType = board.getBoardType();
            this.images = imageFiles.stream()
                                    .map(ImageFileDto.ImageFileResponse::new)
                                    .collect(Collectors.toList());
        }
    }
}
