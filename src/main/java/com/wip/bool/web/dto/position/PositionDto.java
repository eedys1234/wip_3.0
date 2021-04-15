package com.wip.bool.web.dto.position;

import com.wip.bool.domain.cmmn.CodeModel;
import com.wip.bool.domain.position.Position;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class PositionDto {

    @Getter
    @NoArgsConstructor
    public static class PositionBase {

        @NotBlank
        protected String positionName;

    }

    @Getter
    @NoArgsConstructor
    public static class PositionSaveRequest extends PositionBase {

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
    @NoArgsConstructor
    public static class PositionUpdateRequest extends PositionBase {

        @Builder
        public PositionUpdateRequest(String positionName) {
            this.positionName = positionName;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class PositionResponse extends PositionBase implements CodeModel {

        private Long positionId;

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
