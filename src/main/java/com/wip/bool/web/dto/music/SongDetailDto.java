package com.wip.bool.web.dto.music;

import com.wip.bool.domain.music.SongDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class SongDetailDto {

    @Getter
    @NoArgsConstructor
    public static class SongDetailSaveRequest {

        @NotBlank
        private String title;

        @NotBlank
        private String lyrics;

        @NotNull
        private Long codeKey;

        @NotNull
        private Long guitarCodeKey;

        @NotNull
        private Long wordsMasterKey;

        @Builder
        public SongDetailSaveRequest(String title, String lyrics, Long codeKey, Long guitarCodeKey, Long wordsMasterKey) {
            this.title = title;
            this.lyrics = lyrics;
            this.codeKey = codeKey;
            this.guitarCodeKey = guitarCodeKey;
            this.wordsMasterKey = wordsMasterKey;
        }
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

        private Long codeKey;

        private Long guitarCodeKey;

        private LocalDateTime createDate;

        private LocalDateTime modifyDate;

        public SongDetailResponse(SongDetail songDetail) {
            this.id = songDetail.getId();;
            this.title = songDetail.getTitle();
            this.codeKey = songDetail.getSongMaster().getId();
            this.guitarCodeKey = songDetail.getGuitarCode().getId();
            this.createDate = songDetail.getCreateDate();
            this.modifyDate = songDetail.getModifyDate();
        }

    }
}
