package com.wip.bool.cmmn.dept;

import com.wip.bool.dept.domain.Dept;
import org.springframework.test.util.ReflectionTestUtils;

public class DeptFactory {

    public static Dept getDept() {
        Dept dept = Dept.builder()
                        .deptName("밍공")
                        .build();

        return dept;
    }

    public static Dept getDept(long id) {
        Dept dept = getDept();
        ReflectionTestUtils.setField(dept, "id", id);
        return dept;
    }

    public static Dept getDept(String deptName) {
        Dept dept = Dept.builder()
                        .deptName(deptName)
                        .build();

        return dept;
    }

    public static Dept getDept(String deptName, long id) {
        Dept dept = getDept(deptName);
        ReflectionTestUtils.setField(dept, "id", id);
        return dept;
    }

}
