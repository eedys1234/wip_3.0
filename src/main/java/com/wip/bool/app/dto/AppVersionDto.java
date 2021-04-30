package com.wip.bool.app.dto;

import com.wip.bool.app.domain.AppVersion;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class AppVersionDto {

    @Getter
    @NoArgsConstructor
    public static class AppVersionSaveRequest {

        @NotBlank
        private String name;

        @NotBlank
        private String version;

        public AppVersionSaveRequest(String name, String version) {
            this.name = name;
            this.version = version;
        }

    }

    @Getter
    @NoArgsConstructor
    public static class AppVersionResponse {

        private String name;

        private String version;

        public AppVersionResponse(AppVersion appVersion) {
            this.name = appVersion.getName();
            this.version = appVersion.getVersion();
        }
    }
}
