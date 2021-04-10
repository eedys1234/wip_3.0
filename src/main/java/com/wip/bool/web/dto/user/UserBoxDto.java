package com.wip.bool.web.dto.user;

import com.wip.bool.domain.user.UserBox;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserBoxDto {

    @Getter
    @NoArgsConstructor
    public static class UserBoxSaveRequest {

        private String userBoxName;
    }

    @Getter
    @NoArgsConstructor
    public static class UserBoxUpdateRequest {

        private String userBoxName;
    }

    @Getter
    @NoArgsConstructor
    public static class UserBoxResponse {

        private Long userBoxId;
        private Long userId;
        private String userBoxName;

        public UserBoxResponse(UserBox userBox) {

            this.userBoxId = userBox.getId();
            this.userId = userBox.getUser().getId();
            this.userBoxName = userBox.getUserBoxName();

        }

    }
}
