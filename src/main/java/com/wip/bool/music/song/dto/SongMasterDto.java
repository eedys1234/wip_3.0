package com.wip.bool.music.song.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.music.song.domain.SongMaster;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class SongMasterDto {

    @Getter
    @NoArgsConstructor
    public static class SongMasterSaveRequest {

        @JsonProperty(value = "code_name")
        @NotBlank
        private String codeName;
    }

    @Getter
    @NoArgsConstructor
    public static class SongMasterUpdateRequest {

        @JsonProperty(value = "code_name")
        @NotBlank
        private String codeName;
    }

    @Getter
    @NoArgsConstructor
    public static class SongMasterResponse {

        private Long id;

        @JsonProperty(value = "code_name")
        private String codeName;

        @JsonProperty(value = "code_key")
        private String codeKey;

        public SongMasterResponse(SongMaster songMaster) {
            this.id = songMaster.getId();
            this.codeName = songMaster.getCodeName();
            this.codeKey = songMaster.getCodeKey();
        }
    }
}
