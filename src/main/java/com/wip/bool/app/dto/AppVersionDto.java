package com.wip.bool.app.dto;

import com.wip.bool.app.domain.AppVersion;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class AppVersionDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class AppVersionSaveRequest {

        @NotBlank
        private String name;

        @NotBlank
        private String version;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class AppVersionResponse {

        private Long appVersionId;

        private String name;

        private String version;

        public AppVersionResponse(AppVersion appVersion) {
            this.appVersionId = appVersion.getId();
            this.name = appVersion.getName();
            this.version = appVersion.getVersion();
        }
    }
}
