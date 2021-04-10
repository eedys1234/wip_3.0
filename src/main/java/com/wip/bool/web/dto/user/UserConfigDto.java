package com.wip.bool.web.dto.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserConfigDto {

    @Getter
    @NoArgsConstructor
    public static class UserConfigUpdateRequest {

        private String fontSize;
        private String viewType;
        private String recvAlaram;

    }
}
