package com.wip.bool.dept.repository;

import com.wip.bool.cmmn.dept.DeptFactory;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.dept.domain.Dept;
import com.wip.bool.dept.domain.DeptRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.wip.bool.cmmn.util.WIPProperty.TEST;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles(TEST)
@Transactional
public class DeptRepositoryTest {

    @Autowired
    private DeptRepository deptRepository;

    @DisplayName("부서 추가")
    @Test
    public void 부서_추가_Repository() throws Exception {

        //given
        Dept dept = DeptFactory.getDept();

        //when
        Dept addDept = deptRepository.save(dept);

        //then
        assertThat(addDept.getId()).isGreaterThan(0L);
        assertThat(addDept.getDeptName()).isEqualTo(dept.getDeptName());
    }

    @DisplayName("부서 수정")
    @Test
    public void 부서_수정_Repository() throws Exception {

        //given
        Dept dept = DeptFactory.getDept();
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
        Dept dept = DeptFactory.getDept();
        Dept addDept = deptRepository.save(dept);

        //when
        deptRepository.delete(addDept);

        //then
        List<Dept> depts = deptRepository.findAll();
        assertThat(depts.size()).isEqualTo(0);
    }

    @DisplayName("부서 리스트 조회")
    @Test
    public void 부서_리스트_조회_Repository() throws Exception {

        //given
        List<Dept> depts = DeptFactory.getDepts();

        for(Dept dept : depts)
        {
            deptRepository.save(dept);
        }

        //when
        List<Dept> values = deptRepository.findAll();

        //then
        assertThat(values.size()).isEqualTo(depts.size());
        assertThat(values).extracting(Dept::getDeptName)
                .contains(DeptFactory.deptNames);
    }
}
