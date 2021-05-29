package com.wip.bool.bookmark.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

        @JsonProperty(value = "guitar_code")
        private String guitarCode;

        public BookMarkResponse(Long bookMarkId, Long songDetailId, String title, String guitarCode, LocalDateTime createDate) {
            this.bookmarkId = bookMarkId;
            this.songDetailId = songDetailId;
            this.title = title;
            this.guitarCode = guitarCode;
            this.createDate = createDate;
        }
    }
}
