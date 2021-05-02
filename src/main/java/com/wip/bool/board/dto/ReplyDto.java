package com.wip.bool.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class ReplyDto {

    @Getter
    @NoArgsConstructor
    public static class ReplySaveRequest {

        @NotBlank
        private String content;

        @Positive
        private Long boardId;

        @Positive
        private Long parentId;

        private String orgFileNames;

        private String tempFileNames;

    }

    @Getter
    @NoArgsConstructor
    public static class ReplyResponse {

        private Long replyId;

        private String content;

        private String filePath;

        private String newFileName;

        private String fileExt;

        private Long boardId;

        private Long parentId;

        public ReplyResponse(Long replyId, String content, String filePath, String newFileName, String fileExt,
                             Long boardId, Long parentId) {

            this.replyId = replyId;
            this.content = content;
            this.filePath = filePath;
            this.newFileName = newFileName;
            this.fileExt = fileExt;
            this.boardId = boardId;
            this.parentId = parentId;
        }

    }
}
