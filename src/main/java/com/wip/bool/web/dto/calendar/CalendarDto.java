package com.wip.bool.web.dto.calendar;

import com.wip.bool.domain.calendar.ShareType;
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

        @NotBlank
        private String shareType;

        @Positive
        private Long calendarDate;

    }

    @Getter
    @NoArgsConstructor
    public static class CalendarResponse {

        private Long calendarId;

        private String title;

        private String content;

        private LocalDateTime calendarDate;

        private ShareType shareType;

        private String userEmail;

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
