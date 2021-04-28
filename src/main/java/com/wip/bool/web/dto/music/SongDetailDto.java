package com.wip.bool.web.dto.music;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wip.bool.domain.music.song.SongDetail;
import com.wip.bool.domain.bookmark.BookMark;
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
        private Long codeId;

        @NotNull
        private Long guitarCodeId;

        @NotNull
        private Long wordsMasterId;

        @Builder
        public SongDetailSaveRequest(String title, String lyrics, Long codeId, Long guitarCodeId, Long wordsMasterId) {
            this.title = title;
            this.lyrics = lyrics;
            this.codeId = codeId;
            this.guitarCodeId = guitarCodeId;
            this.wordsMasterId = wordsMasterId;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class SongDetailUpdateRequest {

        private String title;

        private String lyrics;

        private Long codeId;

        private Long guitarCodeId;

        private Long wordsMasterId;

        @Builder
        public SongDetailUpdateRequest(String title, String lyrics, Long codeId, Long guitarCodeId, Long wordsMasterId) {

            this.title = title;
            this.lyrics = lyrics;
            this.codeId = codeId;
            this.guitarCodeId = guitarCodeId;
            this.wordsMasterId = wordsMasterId;
        }
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
    public static class SongDetailSimpleResponse {

        private Long id;

        private String title;

        @JsonIgnore
        private Long guitarCodeId;

        public SongDetailSimpleResponse(String title) {
            this(null, title);
        }

        public SongDetailSimpleResponse(Long id, String title) {
            this(id, title, null);
        }

        public SongDetailSimpleResponse(Long id, String title, Long guitarCodeId) {
            this.id = id;
            this.title = title;
            this.guitarCodeId = guitarCodeId;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class SongDetailResponse {

        private Long id;

        private String title;

        private Long codeId;

        private Long guitarCodeId;

        private String guitarCode;

        private Long bookmarkId;

        private LocalDateTime createDate;

        private LocalDateTime modifyDate;


        public SongDetailResponse(SongDetail songDetail) {
            this.id = songDetail.getId();;
            this.title = songDetail.getTitle();
            this.codeId = songDetail.getSongMaster().getId();
            this.guitarCodeId = songDetail.getGuitarCode().getId();
            this.createDate = songDetail.getCreateDate();
            this.modifyDate = songDetail.getModifyDate();
        }

        public SongDetailResponse(SongDetail songDetail, BookMark bookMark) {
            this.id = songDetail.getId();;
            this.title = songDetail.getTitle();
            this.codeId = songDetail.getSongMaster().getId();
            this.guitarCodeId = songDetail.getGuitarCode().getId();
            this.createDate = songDetail.getCreateDate();
            this.modifyDate = songDetail.getModifyDate();
            this.bookmarkId = bookMark.getId();
            this.guitarCode = songDetail.getGuitarCode().getCode();
        }

    }
}
