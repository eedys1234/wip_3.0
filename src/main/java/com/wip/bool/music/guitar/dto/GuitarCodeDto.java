package com.wip.bool.music.guitar.dto;

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

        private Long guitarCodeId;
        private String code;
        private int codeOrder;

        public GuitarCodeResponse(GuitarCode guitarCode) {
            this.guitarCodeId = guitarCode.getId();
            this.code = guitarCode.getCode();
            this.codeOrder = guitarCode.getGuitarOrder();
        }
    }

}
