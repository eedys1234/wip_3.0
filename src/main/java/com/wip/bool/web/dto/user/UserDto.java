package com.wip.bool.web.dto.user;

import com.wip.bool.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {

    @Getter
    @NoArgsConstructor
    public static class UserSaveRequest {

        @NotBlank
        private String email;

        @NotBlank
        private String userPassword;

        private String name;
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
    public static class UserLoginRequest {

        @NotBlank
        private String email;

        @NotBlank
        private String userPassword;
    }

    @Getter
    @NoArgsConstructor
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
