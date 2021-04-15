package com.wip.bool.web.dto.user;

import com.wip.bool.domain.user.UserBox;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class UserBoxDto {

    @Getter
    @NoArgsConstructor
    public static class UserBoxBase {

        @NotBlank
        protected String userBoxName;
    }

    @Getter
    @NoArgsConstructor
    public static class UserBoxSaveRequest extends UserBoxBase {

    }

    @Getter
    @NoArgsConstructor
    public static class UserBoxUpdateRequest extends UserBoxBase {

    }

    @Getter
    @NoArgsConstructor
    public static class UserBoxResponse extends UserBoxBase {

        private Long userBoxId;
        private Long userId;

        public UserBoxResponse(UserBox userBox) {

            this.userBoxId = userBox.getId();
            this.userId = userBox.getUser().getId();
            this.userBoxName = userBox.getUserBoxName();

        }

    }
}
