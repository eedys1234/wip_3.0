package com.wip.bool.music.guitar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.music.guitar.domain.GuitarCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class GuitarCodeDto {

    @Getter
    @NoArgsConstructor
    public static class GuitarCodeSaveRequest {

        @NotBlank
        private String code;
    }

    @Getter
    @NoArgsConstructor
    public static class GuitarCodeResponse {

        @JsonProperty(value = "guitar_code_id")
        private Long guitarCodeId;

        private String code;

        @JsonProperty(value = "code_order")
        private int codeOrder;

        public GuitarCodeResponse(GuitarCode guitarCode) {
            this.guitarCodeId = guitarCode.getId();
            this.code = guitarCode.getCode();
            this.codeOrder = guitarCode.getGuitarOrder();
        }
    }

}
