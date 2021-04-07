package com.wip.bool.web.dto.user;

import com.wip.bool.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class UserDto {

    @Getter
    @NoArgsConstructor
    public static class UserRegisterRequest {

        @NotBlank
        private String userId;

        @NotBlank
        private String userPassword;

        @NotBlank
        private Long deptId;

        @NotBlank
        private Long positionId;

    }

    @Getter
    @NoArgsConstructor
    public static class UserUpdateRequest {

        private String userPassword;

        private Long deptId;

        private Long positionId;

        private String role;
    }

    @Getter
    @NoArgsConstructor
    public static class UserResponse {

        private String userId;

        private String role;

        private String deptName;

        private String positionName;

        private LocalDateTime createDate;

        public UserResponse(User user) {
            this.userId = user.getUserId();
            this.role = user.getRole().toString();
            this.createDate = user.getCreateDate();
        }
    }
}
