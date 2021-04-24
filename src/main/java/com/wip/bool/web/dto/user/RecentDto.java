package com.wip.bool.web.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class RecentDto {

    @Getter
    @NoArgsConstructor
    public static class RecentResponse {

        private Long recentId;

        private Long songDetailId;

        private String title;

        private LocalDateTime createDate;

        public RecentResponse(Long recentId, Long songDetailId, String title, LocalDateTime createDate) {
            this.recentId = recentId;
            this.songDetailId = songDetailId;
            this.title = title;
            this.createDate = createDate;
        }
    }
}
