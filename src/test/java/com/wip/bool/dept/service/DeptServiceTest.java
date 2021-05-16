package com.wip.bool.dept.service;

import com.wip.bool.cmmn.dept.DeptFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.dept.domain.Dept;
import com.wip.bool.dept.domain.DeptRepository;
import com.wip.bool.dept.dto.DeptDto;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeptServiceTest {

    @InjectMocks
    private DeptService deptService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeptRepository deptRepository;

    private User getNormalUser() {
        User user = UserFactory.getNormalUser();
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private User getAdminUser() {
        User user = UserFactory.getAdminUser();
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private Dept getDept() {
        Dept dept = DeptFactory.getDept();
        ReflectionTestUtils.setField(dept, "id", 1L);
        return dept;
    }


    private Dept getDept(String deptName, Long id) {
        Dept dept = DeptFactory.getDept(deptName);
        ReflectionTestUtils.setField(dept, "id", id);
        return dept;
    }

    @DisplayName("부서 추가")
    @Test
    public void 부서_추가_Service() throws Exception {

        //given
        User user = getAdminUser();
        Dept dept = getDept();
        DeptDto.DeptSaveRequest requestDto = new DeptDto.DeptSaveRequest();
        ReflectionTestUtils.setField(dept, "deptName", dept.getDeptName());

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(dept).when(deptRepository).save(any(Dept.class));
        Long id = deptService.saveDept(user.getId(), requestDto);

        //then
        assertThat(id).isGreaterThan(0L);

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(deptRepository, times(1)).save(any(Dept.class));
    }

    @DisplayName("부서 수정")
    @Test
    public void 부서_수정_Service() throws Exception {

        //given
        User user = getAdminUser();
        Dept dept = getDept();
        String deptName = "팡공";
        dept.update(deptName);

        DeptDto.DeptUpdateRequest requestDto = new DeptDto.DeptUpdateRequest();

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(dept)).when(deptRepository).findById(any(Long.class));
        doReturn(dept).when(deptRepository).save(any(Dept.class));
        Long id = deptService.updateDept(user.getId(), dept.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(dept.getId());
        
        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(deptRepository, times(1)).findById(any(Long.class));
    }

    @DisplayName("부서 삭제")
    @Test
    public void 부서_삭제_Service() throws Exception {

        //given
        User user = getAdminUser();
        Dept dept = getDept();

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(dept)).when(deptRepository).findById(any(Long.class));
        doNothing().when(deptRepository).delete(any(Dept.class));
        Long resValue = deptService.deleteDept(user.getId(), dept.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(deptRepository, times(1)).delete(any(Dept.class));
    }

    @DisplayName("부서 리스트 조회")
    @Test
    public void 부서_리스트_조회_Service() throws Exception {

        //given
        List<Dept> depts = new ArrayList<>();
        String[] deptNames = {"팡공", "밍공", "볼공", "맹공"};
        int cnt = 4;

        for(int i=1;i<=cnt;i++) {
            Dept dept = getDept(deptNames[i-1], (long)i);
            depts.add(dept);
        }

        //when
        doReturn(depts).when(deptRepository).findAll();
        List<DeptDto.DeptResponse> values = deptService.findAll();

        //then
        assertThat(values.size()).isEqualTo(cnt);
        assertThat(values).extracting(DeptDto.DeptResponse::getDeptName).contains(deptNames);

        //verify
        verify(deptRepository, times(1)).findAll();
    }
}
