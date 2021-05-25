package com.wip.bool.user.service;

import com.wip.bool.cmmn.dept.DeptFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.dept.domain.Dept;
import com.wip.bool.dept.domain.DeptRepository;
import com.wip.bool.position.domain.PositionRepository;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeptRepository deptRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @DisplayName("회원가입")
    @Test
    public void 회원가입_Service() throws Exception {

        //given
        User user = UserFactory.getRequestUser(1L);

        UserDto.UserSaveRequest requestDto = new UserDto.UserSaveRequest();
        ReflectionTestUtils.setField(requestDto, "email", user.getEmail());
        ReflectionTestUtils.setField(requestDto, "name", user.getName());
        ReflectionTestUtils.setField(requestDto, "userPassword", user.getUserPassword());

        //when
        doReturn(user).when(userRepository).save(any(User.class));
        Long id = userService.join(requestDto);

        //then
        assertThat(id).isGreaterThan(0L);
        assertThat(id).isEqualTo(user.getId());

        //verify
        verify(userRepository, times(1)).save(any(User.class));
    }

    @DisplayName("사용자_수정")
    @Test
    public void 사용자_수정_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        Dept dept = DeptFactory.getDept(1L);

        UserDto.UserUpdateRequest requestDto = new UserDto.UserUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "deptId", dept.getId());

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(dept)).when(deptRepository).findById(anyLong());
        Long id = userService.update(user.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(user.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(deptRepository, times(1)).findById(anyLong());
    }

    @DisplayName("사용자 승인")
    @Test
    public void 사용자_승인_Service() throws Exception {

        //given
        User user = UserFactory.getRequestUser(1L);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        Long id = userService.approve(user.getId());

        //then
        assertThat(id).isEqualTo(user.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
    }

    @DisplayName("사용자 삭제")
    @Test
    public void 사용자_삭제_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(1L).when(userRepository).delete(any(User.class));
        Long resValue = userService.delete(user.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).delete(any(User.class));
    }

    @DisplayName("사용자 조회 by Role")
    @Test
    public void 사용자_조회_byRole_Service() throws Exception {

        //given
        User userA = UserFactory.getRequestUser(1L);
        User userB = UserFactory.getRequestUser(2L);
        List<User> users = new ArrayList<>();

        users.add(userA);
        users.add(userB);

        Role role = Role.ROLE_REQUEST;

        //when
        doReturn(users).when(userRepository).findAllByRole(any(Role.class));
        List<UserDto.UserResponse> values = userService.findAllByRole(role);

        //then
        assertThat(values.size()).isEqualTo(users.size());

        //verify
        verify(userRepository, times(1)).findAllByRole(any(Role.class));
    }

    @DisplayName("사용자 리스트 조회")
    @Test
    public void 사용자_리스트_조회_Service() throws Exception {

        //given
        List<User> users = UserFactory.getNormalUsersWithId();

        //when
        doReturn(users).when(userRepository).findAll();
        List<UserDto.UserResponse> values = userService.findAll();

        //then
        assertThat(values.size()).isEqualTo(users.size());
        assertThat(values).extracting(UserDto.UserResponse::getId)
                .containsAll(users.stream().map(User::getId).collect(Collectors.toList()));

        //verify
        verify(userRepository, times(1)).findAll();
    }

}
