package com.wip.bool.cmmn.dept;

import com.wip.bool.dept.domain.Dept;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DeptFactory {

    public static String[] deptNames = {"밍공", "팡공", "볼공", "맹공"};

    public static List<Dept> getDepts() {
        return Arrays.stream(deptNames)
                .map(deptName -> getDept(deptName))
                .collect(Collectors.toList());
    }

    public static List<Dept> getDeptsWithId() {
        AtomicInteger index = new AtomicInteger(1);
        return Arrays.stream(deptNames)
                .map(deptName -> {
                    Dept dept = getDept(deptName, index.intValue());
                    index.incrementAndGet();
                    return dept;
                })
                .collect(Collectors.toList());
    }

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
