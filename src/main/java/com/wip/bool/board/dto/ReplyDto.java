package com.wip.bool.board.dto;

import com.wip.bool.cmmn.status.DeleteStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

public class ReplyDto {

    @Getter
    @NoArgsConstructor
    public static class ReplySaveRequest {

        @NotBlank
        private String content;

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

        private List<ReplyResponse> nodes = new ArrayList<>();

        public ReplyResponse(Long replyId, String content, DeleteStatus isDeleted, String filePath, String newFileName, String fileExt,
                             Long boardId, Long parentId) {

            this.replyId = replyId;
            this.content = isDeleted == DeleteStatus.DELETE ? "삭제된 댓글입니다." : content;
            this.filePath = filePath;
            this.newFileName = newFileName;
            this.fileExt = fileExt;
            this.boardId = boardId;
            this.parentId = parentId;
        }

    }
}
