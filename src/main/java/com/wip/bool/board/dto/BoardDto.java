package com.wip.bool.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.board.domain.Board;
import com.wip.bool.board.domain.BoardType;
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

        @JsonProperty(value = "board_type")
        @NotBlank
        private String boardType;

        @JsonProperty(value = "org_file_names")
        private String orgFileNames;

        @JsonProperty(value = "temp_file_names")
        private String tempFileNames;

    }

    @Getter
    @NoArgsConstructor
    public static class BoardSimpleResponse {

        @JsonProperty(value = "board_id")
        private Long boardId;

        private String title;

        @JsonProperty(value = "board_type")
        private BoardType boardType;

        public BoardSimpleResponse(Long boardId, String title, DeleteStatus isDeleted, BoardType boardType) {
            this.boardId = boardId;
            this.title = isDeleted == DeleteStatus.DELETE ? "숨김처리된 게시글입니다." : title;
            this.boardType = boardType;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class BoardResponse {

        @JsonProperty(value = "board_id")
        private Long boardId;

        @JsonProperty(value = "board_title")
        private String title;

        @JsonProperty(value = "board_content")
        private String content;

        @JsonProperty(value = "user_name")
        private String userName;

        @JsonProperty(value = "user_email")
        private String userEmail;

        @JsonProperty(value = "board_type")
        private BoardType boardType;

        private List<ImageFileDto.ImageFileResponse> images;

        public BoardResponse(Board board) {
            this.boardId = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.boardType = board.getBoardType();

            this.images = board.getImageFiles().stream()
                                    .map(ImageFileDto.ImageFileResponse::new)
                                    .collect(Collectors.toList());

            this.userName = board.getUser().getName();
            this.userEmail = board.getUser().getEmail();
        }
    }
}
