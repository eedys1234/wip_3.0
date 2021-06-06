package com.wip.bool.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

        @JsonProperty(value = "parent_id")
        private Long parentId;

        @JsonProperty(value = "org_file_names")
        private String orgFileNames;

        @JsonProperty(value = "temp_file_names")
        private String tempFileNames;

    }

    @Getter
    @NoArgsConstructor
    public static class ReplyUpdateRequest {

        @NotBlank
        private String content;

        @JsonProperty(value = "org_file_names")
        private String orgFileNames;

        @JsonProperty(value = "temp_file_names")
        private String tempFileNames;

    }

    @Getter
    @NoArgsConstructor
    public static class ReplyResponse {

        @JsonProperty(value = "reply_id")
        private Long replyId;

        private String content;

        @JsonProperty(value = "parent_id")
        private Long parentId;

        @JsonProperty(value = "user_name")
        private String userName;

        @JsonProperty(value = "user_email")
        private String userEmail;

        private List<ImageFileDto.ImageFileResponse> images;

        private List<ReplyResponse> nodes = new ArrayList<>();

        public ReplyResponse(Reply reply) {

            this.replyId = reply.getId();
            this.content = reply.getIsDeleted() == DeleteStatus.DELETE ? "삭제된 댓글입니다." : reply.getContent();

            if(!Objects.isNull(reply.getParentReply())) {
                this.parentId = reply.getParentReply().getId();
            }

            this.images = reply.getImageFiles().stream()
                    .map(ImageFileDto.ImageFileResponse::new)
                    .collect(Collectors.toList());

            this.userName = reply.getUser().getName();
            this.userEmail = reply.getUser().getEmail();
        }
    }
}
