package com.wip.bool.web.dto.music;

import com.wip.bool.domain.music.SongMaster;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class SongMasterDto {

    @Getter
    @NoArgsConstructor
    public static class SongMasterSaveRequest {

        @NotBlank
        private String codeName;
    }

    @Getter
    @NoArgsConstructor
    public static class SongMasterUpdateRequest {

        @NotBlank
        private String codeName;
    }

    @Getter
    @NoArgsConstructor
    public static class SongMasterResponse {

        private Long id;
        private String codeName;
        private String codeKey;

        public SongMasterResponse(SongMaster songMaster) {
            this.id = songMaster.getId();
            this.codeName = songMaster.getCodeName();
            this.codeKey = songMaster.getCodeKey();
        }
    }
}
