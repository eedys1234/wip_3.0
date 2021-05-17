package com.wip.bool.board.dto;

import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.BoardType;
import com.wip.bool.cmmn.status.DeleteStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BoardDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BoardSimpleResponse {

        private Long boardId;

        private String title;

        private BoardType boardType;

        public BoardSimpleResponse(Long boardId, String title, DeleteStatus isDeleted, BoardType boardType) {
            this.boardId = boardId;
            this.title = isDeleted == DeleteStatus.DELETE ? "숨김처리된 게시글입니다." : title;
            this.boardType = boardType;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BoardResponse {

        private Long boardId;

        private String title;

        private String content;

        private BoardType boardType;

        private List<ImageFileDto.ImageFileResponse> images;

        public BoardResponse(Board board) {
            this.boardId = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.boardType = board.getBoardType();

            if(!Objects.isNull(board.getImageFiles())) {
                this.images = board.getImageFiles().stream()
                                        .map(ImageFileDto.ImageFileResponse::new)
                                        .collect(Collectors.toList());
            }
        }
    }
}
