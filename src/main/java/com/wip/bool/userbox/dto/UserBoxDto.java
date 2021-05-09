package com.wip.bool.userbox.dto;

import com.wip.bool.userbox.domain.UserBox;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class UserBoxDto {

    @Getter
    @NoArgsConstructor
    public static class UserBoxSaveRequest {

        @NotBlank
        private String userBoxName;
    }

    @Getter
    @NoArgsConstructor
    public static class UserBoxUpdateRequest {

        @NotBlank
        private String userBoxName;

    }

    @Getter
    @NoArgsConstructor
    public static class UserBoxResponse {

        private String userBoxName;
        private Long userBoxId;
        private Long userId;

        public UserBoxResponse(UserBox userBox) {

            this.userBoxId = userBox.getId();
            this.userId = userBox.getUser().getId();
            this.userBoxName = userBox.getUserBoxName();

        }

    }
}
