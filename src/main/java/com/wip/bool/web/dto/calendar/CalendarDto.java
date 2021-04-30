package com.wip.bool.web.dto.calendar;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CalendarDto {

    @Getter
    @NoArgsConstructor
    public static class CalendarSaveRequest {

        private String title;

        private String content;

        private String shareType;

        private Long calendarDate;

    }

    @Getter
    @NoArgsConstructor
    public static class CalendarResponse {

        private Long calendarId;

        private String title;

        private String content;

        private LocalDateTime calendarDate;

        private String userEmail;

        private Long userId;
    }
}
