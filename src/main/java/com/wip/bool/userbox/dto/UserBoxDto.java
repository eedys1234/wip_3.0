package com.wip.bool.userbox.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.rights.domain.Rights;
import com.wip.bool.userbox.domain.UserBox;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserBoxDto {

    @Getter
    @NoArgsConstructor
    public static class UserBoxSaveRequest {

        @JsonProperty(value = "user_box_name")
        @NotBlank
        private String userBoxName;
    }

    @Getter
    @NoArgsConstructor
    public static class UserBoxUpdateRequest {

        @JsonProperty(value = "user_box_name")
        @NotBlank
        private String userBoxName;
    }

    @Getter
    @NoArgsConstructor
    public static class UserBoxResponse {

        @JsonProperty(value = "user_box_name")
        private String userBoxName;

        @JsonProperty(value = "user_box_id")
        private Long userBoxId;

        @JsonProperty(value = "user_id")
        private Long userId;

        @JsonProperty(value = "right_type")
        private String rightType;

        public UserBoxResponse(UserBox userBox) {
            this.userBoxId = userBox.getId();
            this.userId = userBox.getUser().getId();
            this.userBoxName = userBox.getUserBoxName();
        }

        public UserBoxResponse(UserBox userBox, Long rightType) {
            this.userBoxId = userBox.getId();
            this.userId = userBox.getUser().getId();
            this.userBoxName = userBox.getUserBoxName();
            this.rightType = Arrays.stream(Rights.RightType.values())
                    .filter(right -> (right.getValue() & rightType) == right.getValue())
                    .map(Rights.RightType::name).collect(Collectors.joining(","));
        }

    }
}
