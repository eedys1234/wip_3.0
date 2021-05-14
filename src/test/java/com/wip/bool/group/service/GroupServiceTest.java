package com.wip.bool.group.service;

import com.wip.bool.cmmn.group.GroupFactory;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.group.domain.Group;
import com.wip.bool.group.domain.GroupRepository;
import com.wip.bool.group.dto.GroupDto;
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
public class GroupServiceTest {

    @InjectMocks
    private GroupService groupService;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    private User getUser() {
        User user = UserFactory.getNormalUser();
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private Group getGroup(User user) {
        Group group = GroupFactory.getGroup(user);
        ReflectionTestUtils.setField(group, "id", 1L);
        return group;
    }

    private Group getGroup(User user, Long id) {
        Group group = GroupFactory.getGroup(user);
        ReflectionTestUtils.setField(group, "id", id);
        return group;
    }

    private Group addGroup(User user, Long id) {
        return getGroup(user, id);
    }

    @DisplayName("그룹추가")
    @Test
    public void 그룹추가_Service() throws Exception {

        //given
        User user = getUser();
        Group group = getGroup(user);
        GroupDto.GroupSaveRequest requestDto = new GroupDto.GroupSaveRequest();
        ReflectionTestUtils.setField(requestDto, "groupName", "테스트그룹_1");

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(group).when(groupRepository).save(any(Group.class));
        Long id = groupService.saveGroup(user.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(group.getId());

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @DisplayName("그룹수정")
    @Test
    public void 그룹수정_Service() throws Exception {

        //given
        User user = getUser();
        Group group = getGroup(user);
        GroupDto.GroupUpdateRequest requestDto = new GroupDto.GroupUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "groupName", "테스트그룹_1");

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(group)).when(groupRepository).findById(any(Long.class), any(Long.class));
        doReturn(group).when(groupRepository).save(any(Group.class));
        Long id = groupService.updateGroup(user.getId(), group.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(group.getId());

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(groupRepository, times(1)).findById(any(Long.class), any(Long.class));
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @DisplayName("그룹삭제")
    @Test
    public void 그룹삭제_Service() throws Exception {

        //given
        User user = getUser();
        Group group = getGroup(user);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(group)).when(groupRepository).findById(any(Long.class), any(Long.class));
        doReturn(1L).when(groupRepository).delete(any(Group.class));
        Long resValue = groupService.deleteGroup(user.getId(), group.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(groupRepository, times(1)).findById(any(Long.class), any(Long.class));
        verify(groupRepository, times(1)).delete(any(Group.class));

    }

    @DisplayName("그룹 리스트 조회 by Master")
    @Test
    public void 그룹_리스트_조회_byMaster_Service() throws Exception {

        //given
        User user = getUser();
        List<Group> groups = new ArrayList<>();
        int cnt = 10;
        for(int i=1;i<=cnt;i++) {
            groups.add(addGroup(user, (long) i));
        }

        int size = 10;
        int offset = 0;
        String order = "ASC";

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(groups).when(groupRepository).findAllByMaster(any(Long.class), any(OrderType.class), any(Integer.class), any(Integer.class));

        List<GroupDto.GroupResponse> values = groupService.findAllByMaster(user.getId(), order, size, offset);

        //then
        assertThat(values.size()).isEqualTo(groups.size());

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(groupRepository, times(1)).findAllByMaster(any(Long.class), any(OrderType.class),
                any(Integer.class), any(Integer.class));
    }
}
