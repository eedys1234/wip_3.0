package com.wip.bool.dept.repository;

import com.wip.bool.cmmn.dept.DeptFactory;
import com.wip.bool.dept.domain.Dept;
import com.wip.bool.dept.domain.DeptRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeptRepositoryTest {

    @Autowired
    private DeptRepository deptRepository;

    private Dept getDept() {
        Dept dept = DeptFactory.getDept();
        return dept;
    }

    private Dept getDept(String deptName) {
        Dept dept = DeptFactory.getDept(deptName);
        return dept;
    }

    @DisplayName("부서 추가")
    @Test
    public void 부서_추가_Repository() throws Exception {

        //given
        Dept dept = getDept();

        //when
        Dept addDept = deptRepository.save(dept);

        //then
        assertThat(addDept.getId()).isGreaterThan(0L);
    }

    @DisplayName("부서 수정")
    @Test
    public void 부서_수정_Repository() throws Exception {

        //given
        Dept dept = getDept();
        Dept addDept = deptRepository.save(dept);
        String updateDeptName = "팡공";

        //when
        addDept.update(updateDeptName);

        //then
        List<Dept> depts = deptRepository.findAll();
        Dept findDept = depts.get(0);
        assertThat(findDept.getDeptName()).isEqualTo(updateDeptName);

    }

    @DisplayName("부서 삭제")
    @Test
    public void 부서_삭제_Repository() throws Exception {

        //given
        Dept dept = getDept();
        Dept addDept = deptRepository.save(dept);

        //when
        deptRepository.delete(addDept);
        List<Dept> depts = deptRepository.findAll();

        //then
        assertThat(depts.size()).isEqualTo(0);
    }

    @DisplayName("부서 리스트 조회")
    @Test
    public void 부서_리스트_조회_Repository() throws Exception {

        //given
        int cnt = 4;
        String[] deptNames = {"밍공", "팡공", "볼공", "맹공"};

        for(int i=1;i<=4;i++)
        {
            Dept dept = getDept(deptNames[i-1]);
            deptRepository.save(dept);
        }

        //when
        List<Dept> depts = deptRepository.findAll();

        //then
        assertThat(depts.size()).isEqualTo(cnt);
        assertThat(depts).extracting(Dept::getDeptName)
                .contains(deptNames);
    }
}
