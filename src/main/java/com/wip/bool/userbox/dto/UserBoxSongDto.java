package com.wip.bool.userbox.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class UserBoxSongDto {

    @Getter
    @NoArgsConstructor
    public static class UserBoxSongSaveRequest {

        @JsonProperty(value = "song_detail_id")
        @Positive
        private Long songDetailId;
    }

    @Getter
    @NoArgsConstructor
    public static class UserBoxSongResponse {

        @JsonProperty(value = "userbox_song_id")
        private Long userBoxSongId;

        @JsonProperty(value = "song_detail_id")
        private Long songDetailId;

        private String title;

        @JsonProperty(value = "create_date")
        private LocalDateTime createDate;

        public UserBoxSongResponse(Long userBoxSongId, Long songDetailId, String title, LocalDateTime createDate) {
            this.userBoxSongId = userBoxSongId;
            this.songDetailId = songDetailId;
            this.title = title;
            this.createDate = createDate;
        }
    }
}
