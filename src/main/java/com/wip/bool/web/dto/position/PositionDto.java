package com.wip.bool.web.dto.position;

import com.wip.bool.domain.position.Position;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class PositionDto {

    @Getter
    @NoArgsConstructor
    public static class PositionSaveRequest {

        @NotBlank
        private String positionName;

        public Position toEntity() {
            return Position.builder()
                    .positionName(positionName)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class PositionUpdateRequest {

        @NotBlank
        private String positionName;
    }

    @Getter
    @NoArgsConstructor
    public static class PositionResponse {

        private Long id;

        private String positionName;

        public PositionResponse(Position position) {
            this.id = position.getId();
            this.positionName = position.getPositionName();
        }

    }
}
