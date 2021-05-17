package com.wip.bool.userbox.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.userbox.domain.UserBox;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class UserBoxDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserBoxSaveRequest {

        @JsonProperty(value = "user_box_name")
        @NotBlank
        private String userBoxName;

        @JsonProperty(value = "share_type")
        @NotBlank
        private String shareType;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserBoxUpdateRequest {

        @JsonProperty(value = "user_box_name")
        @NotBlank
        private String userBoxName;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserBoxResponse {

        @JsonProperty(value = "user_box_name")
        private String userBoxName;

        @JsonProperty(value = "user_box_id")
        private Long userBoxId;

        @JsonProperty(value = "user_id")
        private Long userId;

        public UserBoxResponse(UserBox userBox) {
            this.userBoxId = userBox.getId();
            this.userId = userBox.getUser().getId();
            this.userBoxName = userBox.getUserBoxName();
        }

    }
}
