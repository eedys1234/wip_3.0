package com.wip.bool.web.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BookMarkDto {

    @Getter
    @NoArgsConstructor
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
