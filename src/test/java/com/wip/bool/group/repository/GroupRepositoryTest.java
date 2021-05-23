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
@ActiveProfiles(value = WIPProperty.TEST)
@Transactional
public class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("그룹추가")
    @Test
    public void 그룹추가_Repository() throws Exception {

        //given
        User user = UserFactory.getNormalUser();
        User addUser = userRepository.save(user);
        Group group = GroupFactory.getGroup(addUser);

        //when
        Group addGroup = groupRepository.save(group);

        //then
        assertThat(addGroup.getId()).isGreaterThan(0L);
        assertThat(addGroup.getGroupName()).isEqualTo(group.getGroupName());
    }


    @DisplayName("그룹수정")
    @Test
    public void 그룹수정_Repository() throws Exception {

        //given
        User user = UserFactory.getNormalUser();
        User addUser = userRepository.save(user);

        Group group = GroupFactory.getGroup(addUser);
        Group addGroup = groupRepository.save(group);

        String updateGroupName = "테스트그룹_2";

        //when
        addGroup.updateGroupName(updateGroupName);

        //then
        assertThat(addGroup.getGroupName()).isEqualTo(updateGroupName);
    }

    @DisplayName("그룹삭제")
    @Test
    public void 그룹삭제_Repository() throws Exception {

        //given
        User user = UserFactory.getNormalUser();
        User addUser = userRepository.save(user);

        Group group = GroupFactory.getGroup(addUser);
        Group addGroup = groupRepository.save(group);

        //when
        Long resValue = groupRepository.delete(addGroup);

        //then
        assertThat(resValue).isEqualTo(1L);

    }

    @DisplayName("그룹 리스트 조회 by Master")
    @Test
    public void 그룹_리스트_조회_byMaster_Repository() throws Exception {

        //given
        int size = 10;
        int offset = 0;

        User user = UserFactory.getNormalUser();
        userRepository.save(user);

        List<Group> groups = GroupFactory.getGroups(user);

        for(Group group : groups)
        {
            groupRepository.save(group);
        }

        //when
        List<Group> values = groupRepository.findAllByMaster(user.getId(), OrderType.ASC, size, offset);

        //then
        assertThat(values.size()).isEqualTo(groups.size());
        assertThat(values).extracting(Group::getGroupName).containsAll(GroupFactory.getGroupNames());
    }

    @DisplayName("그룹 리스트 조회 by User")
    @Test
    public void 그룹_리스트_조회_byUser_Repository() throws Exception {

        //given
        int size = 10;
        int offset = 0;

        User user = UserFactory.getNormalUser();
        User addUser = userRepository.save(user);

        List<Group> groups = GroupFactory.getGroups(addUser);

        for(Group group : groups) {
            groupRepository.save(group);

            GroupMember groupMember = GroupMemberFactory.getGroupMember(group, addUser);
            groupMemberRepository.save(groupMember);
        }

        //when
        List<Group> values = groupRepository.findAllByUser(user.getId(), OrderType.ASC, size, offset);

        //then
        assertThat(values.size()).isEqualTo(groups.size());
        assertThat(values).extracting(Group::getGroupName).containsAll(GroupFactory.getGroupNames());

    }
}
