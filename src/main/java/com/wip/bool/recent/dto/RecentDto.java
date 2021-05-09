package com.wip.bool.recent.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class RecentDto {

    @Getter
    @NoArgsConstructor
    public static class RecentSaveRequest {

        @Positive
        private Long songDetailId;
    }

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
