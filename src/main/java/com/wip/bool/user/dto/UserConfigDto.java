package com.wip.bool.user.dto;

import com.wip.bool.user.domain.UserConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserConfigDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserConfigUpdateRequest {

        private String fontSize;
        private String viewType;
        private String recvAlaram;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserConfigResponse {

        private String fontSize;
        private String viewType;
        private String recvAlaram;

        public UserConfigResponse(UserConfig userConfig) {
            this.fontSize = userConfig.getFontSize();
            this.viewType = userConfig.getViewType();
            this.recvAlaram = userConfig.getRecvAlaram();;
        }
    }
}
