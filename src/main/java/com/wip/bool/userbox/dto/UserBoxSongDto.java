package com.wip.bool.userbox.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class UserBoxSongDto {

    @Getter
    @NoArgsConstructor
    public static class UserBoxSongSaveRequest {

        @Positive
        private Long songDetailId;

        @Positive
        private Long userBoxId;
    }

    @Getter
    @NoArgsConstructor
    public static class UserBoxSongResponse {

        private Long userBoxSongId;

        private Long songDetailId;

        private String title;

        private LocalDateTime createDate;

        public UserBoxSongResponse(Long userBoxSongId, Long songDetailId, String title, LocalDateTime createDate) {
            this.userBoxSongId = userBoxSongId;
            this.songDetailId = songDetailId;
            this.title = title;
            this.createDate = createDate;
        }
    }
}
