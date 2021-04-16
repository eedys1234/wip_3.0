package com.wip.bool.web.dto.music;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class SongDto {

    @Getter
    @NoArgsConstructor
    public static class SongDetailBase {

        @NotBlank
        protected String title;

        @NotBlank
        protected String lyrics;

        @NotBlank @Positive @Min(0)
        protected Long codeKey;

        @NotBlank @Positive @Min(0)
        protected Long guitarCodeKey;

        @NotBlank @Positive @Min(0)
        protected Long wordsMasterKey;

    }

    @Getter
    @NoArgsConstructor
    public static class SongDetailSaveRequest extends SongDetailBase {

    }

    @Getter
    @NoArgsConstructor
    public static class SongDetailUpdateRequest {

        private String title;

        private String lyrics;

        private Long codeKey;

        private Long guitarCodeKey;

        private Long wordsMasterKey;

    }

    @Getter
    @NoArgsConstructor
    public static class SongDetailResponse extends SongDetailBase {

        private Long id;

        private String sheetPath;

        private String mp3Path;

        private LocalDateTime createDate;

        private LocalDateTime modifyDate;
    }
}
