package com.wip.bool.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.app.domain.AppVersion;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppVersionDto {

    @Getter
    @NoArgsConstructor
    public static class AppVersionSaveRequest {

        @NotBlank
        private String name;

        @NotBlank
        private String version;
    }

    @Getter
    @NoArgsConstructor
    public static class AppVersionResponse implements Serializable {

        @JsonProperty(value = "app_version_id")
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
