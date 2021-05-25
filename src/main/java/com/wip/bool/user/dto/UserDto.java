package com.wip.bool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;

public class UserDto {

    @Getter
    @NoArgsConstructor
    public static class UserSaveRequest {

        @NotBlank
        private String email;

        @JsonProperty(value = "user_password")
        private String userPassword;

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
    public static class UserLoginRequest {

        @NotBlank
        private String email;

        @JsonProperty(value = "user_password")
        @NotBlank
        private String userPassword;
    }

    @Getter
    @NoArgsConstructor
    public static class UserResponse {

        private Long id;

        private String email;

        private String role;

        @JsonProperty(value = "dept_id")
        private Long deptId;

        @JsonProperty(value = "dept_name")
        private String deptName;

        @JsonProperty(value = "position_id")
        private Long positionId;

        @JsonProperty(value = "position_name")
        private String positionName;

        @JsonProperty(value = "create_date")
        private LocalDateTime createDate;

        public UserResponse(User user) {

            this.id = user.getId();
            this.email = user.getEmail();
            this.role = user.getRole().toString();
            this.createDate = user.getCreateDate();

            if(!Objects.isNull(user.getDept())) {
                this.deptId = user.getDept().getId();
                this.deptName = user.getDept().getDeptName();
            }

            if(!Objects.isNull(user.getPosition())) {
                this.positionId = user.getPosition().getId();
                this.positionName = user.getPosition().getPositionName();
            }
        }
    }


}
