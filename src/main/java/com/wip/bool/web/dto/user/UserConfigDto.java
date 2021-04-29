package com.wip.bool.web.dto.user;

import com.wip.bool.domain.user.UserConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserConfigDto {

    @Getter
    @NoArgsConstructor
    public static class UserConfigUpdateRequest {

        private String fontSize;
        private String viewType;
        private String recvAlaram;

    }

    @Getter
    @NoArgsConstructor
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
