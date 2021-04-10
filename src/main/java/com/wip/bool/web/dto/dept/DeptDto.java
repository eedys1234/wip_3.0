package com.wip.bool.web.dto.dept;

import com.wip.bool.domain.cmmn.CodeModel;
import com.wip.bool.domain.dept.Dept;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeptDto {

    @Getter
    @NoArgsConstructor
    public static class DeptSaveRequest {

        @NotBlank
        private String deptName;

        public Dept toEntity() {
            return Dept.builder()
                    .deptName(deptName)
                    .build();
        }

        @Builder
        public DeptSaveRequest(String deptName) {
            this.deptName = deptName;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class DeptUpdateRequest {

        @NotBlank
        private String deptName;

        @Builder
        public DeptUpdateRequest(String deptName) {
            this.deptName = deptName;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class DeptResponse implements CodeModel {

        private Long deptId;

        private String deptName;

        public DeptResponse(Dept dept) {
            this.deptId = dept.getId();
            this.deptName = dept.getDeptName();
        }

        @Override
        public Long getKey() {
            return this.deptId;
        }

        @Override
        public String getValue() {
            return this.deptName;
        }
    }
}
