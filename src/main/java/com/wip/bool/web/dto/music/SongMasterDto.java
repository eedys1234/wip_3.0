package com.wip.bool.web.dto.music;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

public class SongMasterDto {

    @Getter
    public static class SongMasterBase {

        @NotBlank
        protected String codeName;
    }

    public static class SongMasterSaveRequest extends SongMasterBase {

    }

    public static class SongMasterUpdateRequest extends SongMasterBase {

    }
}
