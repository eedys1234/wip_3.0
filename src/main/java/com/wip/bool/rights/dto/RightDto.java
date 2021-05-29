package com.wip.bool.rights.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.cmmn.auth.Authority;
import com.wip.bool.cmmn.auth.Target;
import com.wip.bool.rights.domain.Rights;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RightDto {

    @Getter
    @NoArgsConstructor
    public static class RightSaveRequest {

        @JsonProperty(value = "target_id")
        @Positive
        private Long targetId;

        @JsonProperty(value = "target")
        @NotBlank
        private String target;

        @JsonProperty(value = "authority")
        @NotBlank
        private String authority;

        @JsonProperty(value = "authority_id")
        @Positive
        private Long authorityId;

        @JsonProperty(value = "right_type")
        @NotBlank
        private String rightType;

        public Rights toEntity() {
            return Rights.of(Target.valueOf(this.target), targetId, Authority.valueOf(this.authority), authorityId, this.rightType);
        }
    }
}
