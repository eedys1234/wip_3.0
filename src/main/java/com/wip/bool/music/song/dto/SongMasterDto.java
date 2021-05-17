package com.wip.bool.music.song.dto;

import com.wip.bool.music.song.domain.SongMaster;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class SongMasterDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SongMasterSaveRequest {

        @NotBlank
        private String codeName;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SongMasterUpdateRequest {

        @NotBlank
        private String codeName;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
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
