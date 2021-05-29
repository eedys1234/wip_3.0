package com.wip.bool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.user.domain.UserConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserConfigDto {

    @Getter
    @NoArgsConstructor
    public static class UserConfigUpdateRequest {

        @JsonProperty(value = "font_size")
        private String fontSize;

        @JsonProperty(value = "view_type")
        private String viewType;

        @JsonProperty(value = "recv_alaram")
        private String recvAlaram;

    }

    @Getter
    @NoArgsConstructor
    public static class UserConfigResponse {

        @JsonProperty(value = "font_size")
        private String fontSize;

        @JsonProperty(value = "view_type")
        private String viewType;

        @JsonProperty(value = "recv_alaram")
        private String recvAlaram;

        public UserConfigResponse(UserConfig userConfig) {
            this.fontSize = userConfig.getFontSize();
            this.viewType = userConfig.getViewType();
            this.recvAlaram = userConfig.getRecvAlaram();
        }
    }
}
