package com.wip.bool.dept.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.cmmn.CodeModel;
import com.wip.bool.dept.domain.Dept;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class DeptDto {

    @Getter
    @NoArgsConstructor
    public static class DeptSaveRequest {

        @JsonProperty(value = "dept_name")
        @NotBlank
        protected String deptName;

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

        @JsonProperty(value = "dept_name")
        @NotBlank
        protected String deptName;

        @Builder
        public DeptUpdateRequest(String deptName) {
            this.deptName = deptName;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class DeptResponse implements CodeModel, Serializable {

        @JsonProperty(value = "dept_id")
        private Long deptId;

        @JsonProperty(value = "dept_name")
        @NotBlank
        protected String deptName;

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
