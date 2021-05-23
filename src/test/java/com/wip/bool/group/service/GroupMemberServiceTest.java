package com.wip.bool.group.service;

import com.wip.bool.cmmn.group.GroupFactory;
import com.wip.bool.cmmn.group.GroupMemberFactory;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.group.domain.Group;
import com.wip.bool.group.domain.GroupMember;
import com.wip.bool.group.domain.GroupMemberRepository;
import com.wip.bool.group.domain.GroupRepository;
import com.wip.bool.group.dto.GroupMemberDto;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupMemberServiceTest {

    @InjectMocks
    private GroupMemberService groupMemberService;

    @Mock
    private GroupMemberRepository groupMemberRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @DisplayName("그룹멤버 추가")
    @Test
    public void 그룹멤버_추가_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        Group group = GroupFactory.getGroup(user, 1L);
        GroupMember groupMember = GroupMemberFactory.getGroupMember(group, user, 1L);

        GroupMemberDto.GroupMemberSaveRequest requestDto = new GroupMemberDto.GroupMemberSaveRequest();
        ReflectionTestUtils.setField(requestDto, "groupId", group.getId());

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(group)).when(groupRepository).findById(anyLong());
        doReturn(groupMember).when(groupMemberRepository).save(any(GroupMember.class));
        Long id = groupMemberService.saveGroupMember(user.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(user.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(groupRepository, times(1)).findById(anyLong());
        verify(groupMemberRepository, times(1)).save(any(GroupMember.class));
    }

    @DisplayName("그룹멤버 탈퇴")
    @Test
    public void 그룹멤버_탈퇴_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        Group group = GroupFactory.getGroup(user, 1L);
        GroupMember groupMember = GroupMemberFactory.getGroupMember(group, user, 1L);
        Long resValue = 1L;

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(groupMember)).when(groupMemberRepository).findById(anyLong(), anyLong());
        doReturn(1L).when(groupMemberRepository).delete(any(GroupMember.class));
        resValue = groupMemberService.deleteGroupMember(user.getId(), groupMember.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(groupMemberRepository, times(1)).findById(anyLong(), anyLong());
        verify(groupMemberRepository, times(1)).delete(any(GroupMember.class));

    }

    @DisplayName("그룹멤버 조회")
    @Test
    public void 그룹멤버_조회_Service() throws Exception {

        //given
        String order = "ASC";
        int size = 10;
        int offset = 0;

        User userA = UserFactory.getNormalUser(1L);
        Group group = GroupFactory.getGroup(userA, 1L);
        List<User> users =  UserFactory.getNormalUsersWithId();

        List<GroupMember> groupMembers = GroupMemberFactory.getGroupMembers(group, users);

        //when
        doReturn(groupMembers).when(groupMemberRepository).findAllByGroup(anyLong(), any(OrderType.class), anyInt(), anyInt());
        List<GroupMemberDto.GroupMemberResponse> values = groupMemberService.findAllByGroup(group.getId(), order, size, offset);

        //then
        assertThat(values.size()).isEqualTo(groupMembers.size());
        assertThat(values).extracting(GroupMemberDto.GroupMemberResponse::getUserId)
                .containsAll(users.stream().map(User::getId).collect(Collectors.toList()));

        //verify
        verify(groupMemberRepository, times(1)).findAllByGroup(anyLong(), any(OrderType.class), anyInt(), anyInt());
    }
}
