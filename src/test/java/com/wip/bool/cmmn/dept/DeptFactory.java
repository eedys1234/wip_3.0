package com.wip.bool.cmmn.dept;

import com.wip.bool.dept.domain.Dept;

public class DeptFactory {

    public static Dept getDept() {
        Dept dept = Dept.builder()
                        .deptName("밍공")
                        .build();

        return dept;
    }

    public static Dept getDept(String deptName) {
        Dept dept = Dept.builder()
                        .deptName(deptName)
                        .build();

        return dept;
    }
}
