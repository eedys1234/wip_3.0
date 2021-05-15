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

import java.util.ArrayList;
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

    private User getNormalUser() {
        User user = UserFactory.getNormalUser();
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private User getNormalUser(String email, long id) {
        User user = UserFactory.getNormalUser();
        ReflectionTestUtils.setField(user, "email", email);
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    private Group getGroup(User user) {
        Group group = GroupFactory.getGroup(user);
        ReflectionTestUtils.setField(group, "id", 1L);
        return group;
    }

    private GroupMember getGroupMember(Group group , User user) {
        GroupMember groupMember = GroupMemberFactory.getGroupMember(group, user);
        ReflectionTestUtils.setField(groupMember, "id", 1L);
        return groupMember;
    }

    private GroupMember getGroupMember(Group group , User user, long id) {
        GroupMember groupMember = GroupMemberFactory.getGroupMember(group, user);
        ReflectionTestUtils.setField(groupMember, "id", id);
        return groupMember;
    }

    @DisplayName("그룹멤버 추가")
    @Test
    public void 그룹멤버_추가_Service() throws Exception {

        //given
        User user = getNormalUser();
        Group group = getGroup(user);
        GroupMember groupMember = getGroupMember(group, user);

        GroupMemberDto.GroupMemberSaveRequest requestDto = new GroupMemberDto.GroupMemberSaveRequest();
        ReflectionTestUtils.setField(requestDto, "groupId", group.getId());

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(group)).when(groupRepository).findById(any(Long.class));
        doReturn(groupMember).when(groupMemberRepository).save(any(GroupMember.class));
        Long id = groupMemberService.saveGroupMember(user.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(user.getId());


        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(groupRepository, times(1)).findById(any(Long.class));
        verify(groupMemberRepository, times(1)).save(any(GroupMember.class));
    }

    @DisplayName("그룹멤버 탈퇴")
    @Test
    public void 그룹멤버_탈퇴_Service() throws Exception {

        //given
        User user = getNormalUser();
        Group group = getGroup(user);
        GroupMember groupMember = getGroupMember(group, user);
        Long resValue = 1L;

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(groupMember)).when(groupMemberRepository).findById(any(Long.class), any(Long.class));
        doReturn(1L).when(groupMemberRepository).delete(any(GroupMember.class));
        resValue = groupMemberService.deleteGroupMember(user.getId(), groupMember.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(groupMemberRepository, times(1)).findById(any(Long.class), any(Long.class));
        verify(groupMemberRepository, times(1)).delete(any(GroupMember.class));

    }

    @DisplayName("그룹멤버 조회")
    @Test
    public void 그룹멤버_조회_Service() throws Exception {

        //given

        User userA = getNormalUser();
        Group group = getGroup(userA);
        GroupMember groupMember = getGroupMember(group, userA);

        String order = "ASC";
        int size = 10;
        int offset = 0;
        int cnt = 10;
        List<GroupMember> groupMembers = new ArrayList<>();
        List<User> users =  new ArrayList<>();

        for(int i=1;i<=cnt;i++) {
            String email = "test_" + i + "@gmail.com";
            User user = getNormalUser(email, (long)(i+1));
            users.add(user);
            groupMember = getGroupMember(group, user, (long)(i+1));
            groupMembers.add(groupMember);
        }

        //when
        doReturn(groupMembers).when(groupMemberRepository).findAllByGroup(any(Long.class), any(OrderType.class), any(Integer.class), any(Integer.class));
        List<GroupMemberDto.GroupMemberResponse> values = groupMemberService.findAllByGroup(group.getId(), order, size, offset);

        //then
        assertThat(values.size()).isEqualTo(cnt);
        assertThat(values).extracting(GroupMemberDto.GroupMemberResponse::getUserId)
                .containsAll(users.stream().map(User::getId).collect(Collectors.toList()));
    }
}
