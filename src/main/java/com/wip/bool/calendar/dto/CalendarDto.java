package com.wip.bool.calendar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.calendar.repository.Calendar;
import com.wip.bool.cmmn.type.ShareType;
import com.wip.bool.cmmn.util.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

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

        @JsonProperty(value = "calendar_title")
        private String title;

        @JsonProperty(value = "calendar_content")
        private String content;

        @JsonProperty(value = "calendar_date")
        private String calendarDate;

        @JsonProperty(value = "share_type")
        private ShareType shareType;

        @JsonProperty(value = "user_email")
        private String userEmail;

        @JsonProperty(value = "user_name")
        private String userName;

        @JsonProperty(value = "user_id")
        private Long userId;

        public CalendarResponse(Calendar calendar) {

            this.calendarId = calendar.getId();
            this.title = calendar.getTitle();
            this.content = calendar.getContent();
            this.calendarDate = calendar.getCalendarDate().format(DateUtils.YYYY_MM_DD_HH_mm());
            this.shareType = calendar.getShareType();
            this.userEmail = calendar.getUser().getEmail();
            this.userId = calendar.getUser().getId();
            this.userName = calendar.getUser().getName();
        }
    }
}
