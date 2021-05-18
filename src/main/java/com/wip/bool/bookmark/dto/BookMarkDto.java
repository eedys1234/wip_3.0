package com.wip.bool.bookmark.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class BookMarkDto {

    @Getter
    @NoArgsConstructor
    public static class BookMarkSaveRequest {

        @JsonProperty(value = "song_detail_id")
        @Positive
        private Long songDetailId;
    }

    @Getter
    @NoArgsConstructor
    public static class BookMarkResponse {

        @JsonProperty(value = "bookmark_id")
        private Long bookmarkId;

        @JsonProperty(value = "song_detail_id")
        private Long songDetailId;

        private String title;

        @JsonProperty(value = "create_date")
        private LocalDateTime createDate;

        public BookMarkResponse(Long bookMarkId, Long songDetailId, String title, LocalDateTime createDate) {
            this.bookmarkId = bookMarkId;
            this.songDetailId = songDetailId;
            this.title = title;
            this.createDate = createDate;
        }
    }
}
