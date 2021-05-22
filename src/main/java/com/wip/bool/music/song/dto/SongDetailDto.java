package com.wip.bool.music.song.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.bookmark.domain.BookMark;
import com.wip.bool.music.song.domain.SongDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

public class SongDetailDto {

    @Getter
    @NoArgsConstructor
    public static class SongDetailSaveRequest {

        @NotBlank
        private String title;

        @NotBlank
        private String lyrics;

        @JsonProperty(value = "song_master_id")
        @NotNull
        private Long codeId;

        @JsonProperty(value = "guitar_code_id")
        @NotNull
        private Long guitarCodeId;

        @JsonProperty(value = "words_master_id")
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

        @JsonProperty(value = "code_id")
        private Long codeId;

        @JsonProperty(value = "guitar_code_id")
        private Long guitarCodeId;

        @JsonProperty(value = "words_master_id")
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

        @JsonProperty(value = "song_master_id")
        private Long songMasterId;
        private String order;

        @JsonProperty(value = "sort_type")
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

        @JsonProperty(value = "song_master_id")
        private Long codeId;

        @JsonProperty(value = "guitar_code_id")
        private Long guitarCodeId;

        @JsonProperty(value = "guitar_code")
        private String guitarCode;

        @JsonProperty(value = "bookmark_id")
        private Long bookmarkId;

        @JsonProperty(value = "create_date")
        private LocalDateTime createDate;

        @JsonProperty(value = "modify_date")
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
            this.guitarCode = songDetail.getGuitarCode().getCode();

            if(!Objects.isNull(bookMark)) {
                this.bookmarkId = bookMark.getId();
            }
        }

    }
}
