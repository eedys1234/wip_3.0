package com.wip.bool.user.dto;

import com.wip.bool.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class UserDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserBase {

        @NotBlank
        protected String userPassword;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserSaveRequest extends UserBase {

        @NotBlank
        private String email;

        private String name;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserUpdateRequest {

        private String userPassword;

        private Long deptId;

        private Long positionId;

        private String role;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserLoginRequest extends UserBase {

        @NotBlank
        private String email;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserResponse {

        private Long id;

        private String email;

        private String role;

        private String deptName;

        private String positionName;

        private LocalDateTime createDate;

        public UserResponse(User user) {
            this.email = user.getEmail();
            this.role = user.getRole().toString();
            this.createDate = user.getCreateDate();
        }
    }


}
