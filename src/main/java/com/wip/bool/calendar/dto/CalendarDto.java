package com.wip.bool.calendar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.cmmn.type.ShareType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class CalendarDto {

    @Getter
    @NoArgsConstructor
    public static class CalendarSaveRequest {

        @NotBlank
        private String title;

        @NotBlank
        private String content;

        @JsonProperty(value = "share_type")
        @NotBlank
        private String shareType;

        @JsonProperty(value = "calendar_date")
        @Positive
        private Long calendarDate;

    }

    @Getter
    @NoArgsConstructor
    public static class CalendarResponse {

        @JsonProperty(value = "calendar_id")
        private Long calendarId;

        private String title;

        private String content;

        @JsonProperty(value = "calendar_date")
        private LocalDateTime calendarDate;

        @JsonProperty(value = "share_type")
        private ShareType shareType;

        @JsonProperty(value = "user_email")
        private String userEmail;

        @JsonProperty(value = "user_id")
        private Long userId;

        public CalendarResponse(Long calendarId, String title, String content, LocalDateTime calendarDate,
                                ShareType shareType, String userEmail, Long userId) {

            this.calendarId = calendarId;
            this.title = title;
            this.content = content;
            this.calendarDate = calendarDate;
            this.shareType = shareType;
            this.userEmail = userEmail;
            this.userId = userId;
        }
    }
}
