package com.wip.bool.web.dto.user;

import com.wip.bool.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class UserDto {

    @Getter
    @NoArgsConstructor
    public static class UserBase {

        @NotBlank
        protected String userPassword;
    }

    @Getter
    @NoArgsConstructor
    public static class UserSaveRequest extends UserBase {

        @NotBlank
        private String email;

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
    public static class UserLoginRequest extends UserBase {

        @NotBlank
        private String email;

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
