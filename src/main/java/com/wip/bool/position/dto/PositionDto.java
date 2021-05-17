package com.wip.bool.position.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.cmmn.CodeModel;
import com.wip.bool.position.domain.Position;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class PositionDto {


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PositionSaveRequest {

        @JsonProperty(value = "position_name")
        @NotBlank
        protected String positionName;

        public Position toEntity() {
            return Position.builder()
                    .positionName(positionName)
                    .build();
        }

        @Builder
        public PositionSaveRequest(String positionName) {
            this.positionName = positionName;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PositionUpdateRequest {

        @JsonProperty(value = "position_name")
        @NotBlank
        protected String positionName;

        @Builder
        public PositionUpdateRequest(String positionName) {
            this.positionName = positionName;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PositionResponse implements CodeModel {

        @JsonProperty(value = "position_id")
        private Long positionId;

        @JsonProperty(value = "position_name")
        @NotBlank
        protected String positionName;

        public PositionResponse(Position position) {
            this.positionId = position.getId();
            this.positionName = position.getPositionName();
        }

        @Override
        public Long getKey() {
            return this.positionId;
        }

        @Override
        public String getValue() {
            return this.positionName;
        }
    }
}
