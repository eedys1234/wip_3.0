package com.wip.bool.rights.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.cmmn.auth.Authority;
import com.wip.bool.cmmn.auth.Target;
import com.wip.bool.rights.domain.Rights;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

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

        public Rights toEntity() {
            Target target = Target.valueOf(this.target);
            Authority authority = Authority.valueOf(this.authority);

            return Rights.of(target, targetId, authority, authorityId);
        }
    }
}
