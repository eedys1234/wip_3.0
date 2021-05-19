package com.wip.bool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class UserDto {

    @Getter
    @NoArgsConstructor
    public static class UserBase {

        @JsonProperty(value = "user_password")
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

        @JsonProperty(value = "user_password")
        private String userPassword;

        @JsonProperty(value = "dept_id")
        private Long deptId;

        @JsonProperty(value = "position_id")
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

        @JsonProperty(value = "dept_name")
        private String deptName;

        @JsonProperty(value = "position_name")
        private String positionName;

        @JsonProperty(value = "create_date")
        private LocalDateTime createDate;

        public UserResponse(User user) {
            this.email = user.getEmail();
            this.role = user.getRole().toString();
            this.createDate = user.getCreateDate();
        }
    }


}
