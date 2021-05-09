package com.wip.bool.board.dto;

import com.wip.bool.board.domain.ImageFile;
import com.wip.bool.board.domain.Reply;
import com.wip.bool.cmmn.status.DeleteStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReplyDto {

    @Getter
    @NoArgsConstructor
    public static class ReplySaveRequest {

        @NotBlank
        private String content;

        private Long parentId;

        private String orgFileNames;

        private String tempFileNames;

    }

    @Getter
    @NoArgsConstructor
    public static class ReplyResponse {

        private Long replyId;

        private String content;

        private Long parentId;

        private List<ImageFileDto.ImageFileResponse> images;

        private List<ReplyResponse> nodes = new ArrayList<>();

        public ReplyResponse(Reply reply, List<ImageFile> imageFiles) {

            this.replyId = reply.getId();
            this.content = reply.getIsDeleted() == DeleteStatus.DELETE ? "삭제된 댓글입니다." : reply.getContent();

            if(!Objects.isNull(reply.getParentReply())) {
                this.parentId = reply.getParentReply().getId();
            }

            if(!Objects.isNull(imageFiles)) {
                this.images = imageFiles.stream()
                        .map(ImageFileDto.ImageFileResponse::new)
                        .collect(Collectors.toList());
            }
        }

    }
}
