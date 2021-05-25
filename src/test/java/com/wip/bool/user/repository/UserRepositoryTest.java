package com.wip.bool.user.repository;

import com.wip.bool.cmmn.dept.DeptFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.dept.domain.Dept;
import com.wip.bool.dept.domain.DeptRepository;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
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
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeptRepository deptRepository;

    @DisplayName("사용자 추가")
    @Test
    public void 사용자_추가_Repository() throws Exception {
        
        //given
        User user = UserFactory.getNormalUser();

        //when
        User addUser = userRepository.save(user);

        //then
        List<User> users = userRepository.findAll();
        assertThat(addUser.getId()).isGreaterThan(0L);
        assertThat(addUser.getId()).isEqualTo(users.get(0).getId());
        assertThat(addUser.getRole()).isEqualTo(users.get(0).getRole());
        assertThat(addUser.getEmail()).isEqualTo(users.get(0).getEmail());
        assertThat(addUser.getUserType()).isEqualTo(users.get(0).getUserType());
    }

    @DisplayName("사용자 삭제")
    @Test
    public void 사용자_삭제_Repository() throws Exception {

        //given
        User user = UserFactory.getNormalUser();
        User addUser = userRepository.save(user);

        //when
        Long resValue = userRepository.delete(addUser);

        //then
        assertThat(resValue).isEqualTo(1L);
    }

    @DisplayName("사용자 조회 by Role")
    @Test
    public void 사용자_조회_ByRole_Repository() throws Exception {

        //given
        User user = UserFactory.getRequestUser();
        User addUser = userRepository.save(user);

        //when
        List<User> users = userRepository.findAllByRole(Role.ROLE_REQUEST);

        //then
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getRole()).isEqualTo(Role.ROLE_REQUEST);

    }

    @DisplayName("사용자 조회 by Email")
    @Test
    public void 사용자_조회_ByEmail_Repository() throws Exception {

        //given
        User user = UserFactory.getNormalUser();
        User addUser = userRepository.save(user);

        //when
        User byEmail = userRepository.findByEmail(addUser.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_USER));

        //then
        assertThat(byEmail.getId()).isEqualTo(addUser.getId());
        assertThat(byEmail.getEmail()).isEqualTo(addUser.getEmail());
    }

    @DisplayName("사용자 조회 with 부서")
    @Test
    public void 사용자_조회_with부서_Repository() throws Exception {

        //given
        Dept dept = DeptFactory.getDept();
        Dept addDept = deptRepository.save(dept);

        User user = UserFactory.getNormalUser(dept);
        User addUser = userRepository.save(user);

        //when
        User byDept = userRepository.deptByUser(addUser.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_USER));

        //then
        assertThat(byDept.getId()).isEqualTo(addUser.getId());
        assertThat(byDept.getDept().getId()).isEqualTo(dept.getId());
    }

    @DisplayName("사용자 리스트 조회")
    @Test
    public void 사용자_리스트_조회_Repository() throws Exception {

        //given
        User user = UserFactory.getNormalUser();
        User addUser = userRepository.save(user);

        //when
        List<User> users = userRepository.findAll();

        //then
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getId()).isEqualTo(addUser.getId());
    }
}
