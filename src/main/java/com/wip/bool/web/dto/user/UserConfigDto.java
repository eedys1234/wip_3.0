package com.wip.bool.web.dto.user;

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
}
