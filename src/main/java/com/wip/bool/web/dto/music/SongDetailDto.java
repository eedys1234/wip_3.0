package com.wip.bool.web.dto.music;

import com.wip.bool.domain.music.SongDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class SongDetailDto {

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
    public static class SongDetailsRequest {

        private Long songMasterId;
        private String order;
        private String sortType;
    }

    @Getter
    @NoArgsConstructor
    public static class SongDetailResponse {

        private Long id;

        private String title;

        private String codeKey;

        private String guitarCodeKey;

        private LocalDateTime createDate;

        private LocalDateTime modifyDate;

        public SongDetailResponse(SongDetail songDetail) {
            this.id = songDetail.getId();;
            this.title = songDetail.getTitle();
            this.codeKey = songDetail.getSongMaster().getCodeKey();
            this.guitarCodeKey = songDetail.getGuitarCode().getCode();
            this.createDate = songDetail.getCreateDate();
            this.modifyDate = songDetail.getModifyDate();
        }

    }
}
