package com.wip.bool.userbox.service;

import com.wip.bool.cmmn.auth.Authority;
import com.wip.bool.cmmn.auth.Target;
import com.wip.bool.cmmn.dept.DeptFactory;
import com.wip.bool.cmmn.group.GroupFactory;
import com.wip.bool.cmmn.group.GroupMemberFactory;
import com.wip.bool.cmmn.rights.RightsFactory;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.cmmn.userbox.UserBoxFactory;
import com.wip.bool.dept.domain.Dept;
import com.wip.bool.dept.domain.DeptRepository;
import com.wip.bool.group.domain.Group;
import com.wip.bool.group.domain.GroupMember;
import com.wip.bool.group.domain.GroupMemberRepository;
import com.wip.bool.group.domain.GroupRepository;
import com.wip.bool.rights.domain.Rights;
import com.wip.bool.rights.domain.RightsRepository;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.userbox.domain.UserBox;
import com.wip.bool.userbox.domain.UserBoxRepository;
import com.wip.bool.userbox.dto.UserBoxDto;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserBoxServiceTest {

    @InjectMocks
    private UserBoxService userBoxService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserBoxRepository userBoxRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMemberRepository groupMemberRepository;

    @Mock
    private DeptRepository deptRepository;

    @Mock
    private RightsRepository rightRepository;

    private Rights getRight(Long targetId, Long authorityId) {
        Rights right = RightsFactory.getRights(Target.USERBOX, targetId, Authority.USER, authorityId);
        return right;
    }

    @DisplayName("사용자박스 추가")
    @Test
    public void 사용자박스_추가_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        UserBox userBox = UserBoxFactory.getUserBox(user, 1L);
        Rights right = getRight(userBox.getId(), user.getId());
        UserBoxDto.UserBoxSaveRequest requestDto = new UserBoxDto.UserBoxSaveRequest();
        ReflectionTestUtils.setField(requestDto, "userBoxName", "사용자박스_1");

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(userBox).when(userBoxRepository).save(any(UserBox.class));
        doReturn(right).when(rightRepository).save(any(Rights.class));
        Long id = userBoxService.addUserBox(user.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(userBoxRepository, times(1)).save(any(UserBox.class));
    }

    @DisplayName("사용자박스 수정 일반사용자")
    @Test
    public void 사용자박스_수정_일반사용자_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        UserBox userBox = UserBoxFactory.getUserBox(user, 1L);
        UserBox update_userBox = UserBoxFactory.getUserBox(user, 1L);
        update_userBox.updateUserBoxName("사용자박스_2");

        UserBoxDto.UserBoxUpdateRequest requestDto = new UserBoxDto.UserBoxUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "userBoxName", "사용자박스_2");

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(userBox)).when(userBoxRepository).findById(anyLong(), anyLong(), any(Role.class));
        doReturn(update_userBox).when(userBoxRepository).save(any(UserBox.class));
        Long id = userBoxService.updateUserBox(user.getId(), userBox.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(userBox.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(userBoxRepository, times(1)).findById(anyLong(), anyLong(), any(Role.class));
        verify(userBoxRepository, times(1)).save(any(UserBox.class));
    }

    @DisplayName("사용자박스 수정 관리자")
    @Test
    public void 사용자박스_수정_관리자_Service() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        UserBox userBox = UserBoxFactory.getUserBox(user, 1L);
        UserBox update_userBox = UserBoxFactory.getUserBox(user, 1L);
        update_userBox.updateUserBoxName("사용자박스_2");

        UserBoxDto.UserBoxUpdateRequest requestDto = new UserBoxDto.UserBoxUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "userBoxName", "사용자박스_2");

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(userBox)).when(userBoxRepository).findById(anyLong(), anyLong(), any(Role.class));
        doReturn(update_userBox).when(userBoxRepository).save(any(UserBox.class));
        Long id = userBoxService.updateUserBox(user.getId(), userBox.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(userBox.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(userBoxRepository, times(1)).findById(anyLong(), anyLong(), any(Role.class));
        verify(userBoxRepository, times(1)).save(any(UserBox.class));
    }

    @DisplayName("사용자박스 삭제")
    @Test
    public void 사용자박스_삭제_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        UserBox userBox = UserBoxFactory.getUserBox(user, 1L);
        Rights right = getRight(userBox.getId(), user.getId());

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(userBox)).when(userBoxRepository).findById(anyLong(), anyLong(), any(Role.class));
        doReturn(Optional.ofNullable(right)).when(rightRepository).findByTargetIdAndTarget(any(Target.class), anyLong());
        doNothing().when(rightRepository).delete(any(Rights.class));
        doReturn(1L).when(userBoxRepository).delete(any(UserBox.class));
        Long resValue = userBoxService.deleteUserBox(user.getId(), userBox.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(userBoxRepository, times(1)).findById(anyLong(), anyLong(), any(Role.class));
        verify(rightRepository, times(1)).findByTargetIdAndTarget(any(Target.class), anyLong());
        verify(rightRepository, times(1)).delete(any(Rights.class));
        verify(userBoxRepository, times(1)).delete(any(UserBox.class));
    }

    @DisplayName("사용자박스 리스트 조회 by User")
    @Test
    public void 사용자박스_리스트_조회_byUser_Service() throws Exception {

        //given
        String order = "ASC";
        int size = 10;
        int offset = 0;

        User user = UserFactory.getNormalUser(1L);
        List<UserBox> userBoxes = UserBoxFactory.getUserBoxesWithId(user);

        //when
        doReturn(userBoxes.stream()
                .map(userBox -> new UserBoxDto.UserBoxResponse(userBox, 3L))
                .collect(Collectors.toList())).when(userBoxRepository).findAll(any(OrderType.class), anyInt(), anyInt(), anyLong());
        List<UserBoxDto.UserBoxResponse> values = userBoxService.findAllByUser(user.getId(), order, size, offset);

        //then
        assertThat(values.size()).isEqualTo(userBoxes.size());
        assertThat(values).extracting(UserBoxDto.UserBoxResponse::getUserBoxId).containsAll(userBoxes.stream()
                .map(UserBox::getId)
                .collect(Collectors.toList()));

        //verify
        verify(userBoxRepository, times(1)).findAll(any(OrderType.class), anyInt(), anyInt(), anyLong());
    }

    @DisplayName("사용자박스 리스트 조회 by Dept")
    @Test
    public void 사용자박스_리스트_조회_byDept_Service() throws Exception {

        //given
        String order = "ASC";
        int size = 10;
        int offset = 0;
        Dept dept = DeptFactory.getDept(1L);
        User user = UserFactory.getNormalUser(dept, 1L);

        List<UserBox> userBoxes = UserBoxFactory.getUserBoxesWithId(user);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).deptByUser(anyLong());
        doReturn(Optional.ofNullable(dept)).when(deptRepository).findById(anyLong());
        doReturn(userBoxes.stream()
                .map(userBox -> new UserBoxDto.UserBoxResponse(userBox, 1L))
                .collect(Collectors.toList())).when(userBoxRepository).findAll(any(OrderType.class), anyInt(), anyInt(), anyLong());
        List<UserBoxDto.UserBoxResponse> values = userBoxService.findAllByDept(user.getId(), dept.getId(), order, size, offset);

        //then
        assertThat(values.size()).isEqualTo(userBoxes.size());
        assertThat(values).extracting(UserBoxDto.UserBoxResponse::getUserBoxId).containsAll(
                userBoxes.stream()
                        .map(UserBox::getId)
                        .collect(Collectors.toList()));

        //verify
        verify(userBoxRepository, times(1)).findAll(any(OrderType.class), anyInt(), anyInt(), anyLong());

    }

    @DisplayName("사용자박스 리스트 조회 by Group")
    @Test
    public void 사용자박스_리스트_조회_byGroup_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);

        String order = "ASC";
        int size = 10;
        int offset = 0;

        String groupId = "1,2";
        List<UserBox> userBoxes = UserBoxFactory.getUserBoxes(user);
        List<Group> groups = GroupFactory.getGroups(user);
        List<GroupMember> groupMembers = new ArrayList<>();

        for(int i=0;i<2;i++)
        {
            groupMembers.add(GroupMemberFactory.getGroupMember(groups.get(i), user));
        }

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).deptByUser(anyLong());
        doReturn(groupMembers).when(groupMemberRepository).findAllByGroup(any(List.class));
        doReturn(userBoxes.stream()
                .map(userBox -> new UserBoxDto.UserBoxResponse(userBox, 3L))
                .collect(Collectors.toList())).when(userBoxRepository).findAll(any(OrderType.class), anyInt(), anyInt(), any(List.class));

        List<UserBoxDto.UserBoxResponse> values = userBoxService.findAllByGroup(user.getId(), groupId, order, size, offset);

        //then
        assertThat(values.size()).isEqualTo(userBoxes.size());
        assertThat(values).extracting(UserBoxDto.UserBoxResponse::getUserBoxId).containsAll(
                userBoxes.stream()
                        .map(UserBox::getId)
                        .collect(Collectors.toList())
        );

        //verify
        verify(userBoxRepository, times(1)).findAll(any(OrderType.class), anyInt(), anyInt(), any(List.class));
        verify(userRepository, times(1)).deptByUser(anyLong());
        verify(groupMemberRepository, times(1)).findAllByGroup(any(List.class));

    }

    @DisplayName("사용자박스 리스트 조회 by Total")
    @Test
    public void 사용자박스_리스트_조회_byTotal_Service() throws Exception {

        //given
        String order = "ASC";
        int size = 10;
        int offset = 0;
        Dept dept = DeptFactory.getDept(1L);
        User user = UserFactory.getNormalUser(dept, 1L);

        List<Group> groups = GroupFactory.getGroupsWithId(user);
        List<UserBox> userBoxes = UserBoxFactory.getUserBoxesWithId(user);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).deptByUser(anyLong());
        doReturn(groups).when(groupRepository).findAllByUser(anyLong());
        doReturn(userBoxes.stream()
                .map(userBox -> new UserBoxDto.UserBoxResponse(userBox, 3L))
                .collect(Collectors.toList())).when(userBoxRepository).findAll(any(OrderType.class), anyInt(), anyInt(), any(List.class));
        List<UserBoxDto.UserBoxResponse> values = userBoxService.findAllByTotal(user.getId(), order, size, offset);

        //then
        assertThat(values.size()).isEqualTo(userBoxes.size());
        assertThat(values).extracting(UserBoxDto.UserBoxResponse::getUserBoxId).containsAll(
                userBoxes.stream()
                        .map(UserBox::getId)
                        .collect(Collectors.toList())
        );

        //verify
        verify(userRepository, times(1)).deptByUser(anyLong());
        verify(groupRepository, times(1)).findAllByUser(anyLong());
        verify(userBoxRepository, times(1)).findAll(any(OrderType.class), anyInt(), anyInt(), any(List.class));
    }

}
