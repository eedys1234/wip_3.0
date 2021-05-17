package com.wip.bool.bookmark.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class BookMarkDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BookMarkSaveRequest {

        @Positive
        private Long songDetailId;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BookMarkResponse {

        private Long bookmarkId;

        private Long songDetailId;

        private String title;

        private LocalDateTime createDate;

        public BookMarkResponse(Long bookMarkId, Long songDetailId, String title, LocalDateTime createDate) {
            this.bookmarkId = bookMarkId;
            this.songDetailId = songDetailId;
            this.title = title;
            this.createDate = createDate;
        }
    }
}
