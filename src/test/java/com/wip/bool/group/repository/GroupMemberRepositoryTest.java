package com.wip.bool.group.repository;

import com.wip.bool.cmmn.group.GroupFactory;
import com.wip.bool.cmmn.group.GroupMemberFactory;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.cmmn.util.WIPProperty;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.group.domain.Group;
import com.wip.bool.group.domain.GroupMember;
import com.wip.bool.group.domain.GroupMemberRepository;
import com.wip.bool.group.domain.GroupRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles(WIPProperty.TEST)
@Transactional
public class GroupMemberRepositoryTest {

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("그룹멤버 추가")
    @Test
    public void 그룹멤버_추가_Repository() throws Exception {

        //given
        User user = UserFactory.getNormalUser();
        userRepository.save(user);

        Group group = GroupFactory.getGroup(user);
        groupRepository.save(group);

        GroupMember groupMember = GroupMemberFactory.getGroupMember(group, user);

        //when
        GroupMember addGroupMember = groupMemberRepository.save(groupMember);

        //then
        assertThat(addGroupMember.getId()).isGreaterThan(0L);
    }

    @DisplayName("그룹멤버 탈퇴")
    @Test
    public void 그룹멤버_탈퇴_Repository() throws Exception {

        //given
        User user = UserFactory.getNormalUser();
        userRepository.save(user);

        Group group = GroupFactory.getGroup(user);
        groupRepository.save(group);

        GroupMember groupMember = GroupMemberFactory.getGroupMember(group, user);
        GroupMember addGroupMember = groupMemberRepository.save(groupMember);

        //when
        Long resValue = groupMemberRepository.delete(addGroupMember);

        //then
        assertThat(resValue).isEqualTo(1L);
    }
    
    @DisplayName("그룹멤버 조회")
    @Test
    public void 그룹멤버_조회_Repository() throws Exception {

        //given
        int size = 10;
        int cnt = 10;
        int offset = 0;
        String order = "ASC";

        User userA = UserFactory.getNormalUser();
        userRepository.save(userA);

        Group group = GroupFactory.getGroup(userA);
        groupRepository.save(group);

        List<User> users = UserFactory.getNormalUsers();

        for(User user : users)
        {
            userRepository.save(user);
            GroupMember groupMember = GroupMemberFactory.getGroupMember(group, user);
            groupMemberRepository.save(groupMember);
        }

        //when
        List<GroupMember> groupMembers = groupMemberRepository.findAllByGroup(group.getId(), OrderType.valueOf(order), size, offset);

        //then
        assertThat(groupMembers.size()).isEqualTo(cnt);
        assertThat(groupMembers).extracting(GroupMember::getGroup).contains(group);
        assertThat(groupMembers).extracting(GroupMember::getUser).containsAll(users);
    }
}
