package com.wip.bool.recent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class RecentDto {

    @Getter
    @NoArgsConstructor
    public static class RecentSaveRequest {

        @JsonProperty(value = "song_detail_id")
        @Positive
        private Long songDetailId;
    }

    @Getter
    @NoArgsConstructor
    public static class RecentResponse {

        @JsonProperty(value = "recent_id")
        private Long recentId;

        @JsonProperty(value = "song_detail_id")
        private Long songDetailId;

        private String title;

        @JsonProperty(value = "create_date")
        private LocalDateTime createDate;

        public RecentResponse(Long recentId, Long songDetailId, String title, LocalDateTime createDate) {
            this.recentId = recentId;
            this.songDetailId = songDetailId;
            this.title = title;
            this.createDate = createDate;
        }
    }
}
