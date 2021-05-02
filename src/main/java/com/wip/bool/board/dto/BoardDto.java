package com.wip.bool.board.dto;

import com.wip.bool.board.domain.BoardType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

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

        public BoardSimpleResponse(Long boardId, String title, BoardType boardType) {
            this.boardId = boardId;
            this.title = title;
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

        private String filePath;

        private String newFileName;

        private String fileExt;

        public BoardResponse(Long boardId, String title, String content, BoardType boardType, String filePath, String newFileName, String fileExt) {
            this.boardId = boardId;
            this.title = title;
            this.content = content;
            this.boardType = boardType;
        }
    }
}
