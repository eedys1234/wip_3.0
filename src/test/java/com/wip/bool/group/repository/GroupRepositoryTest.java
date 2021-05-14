package com.wip.bool.group.repository;


import com.wip.bool.cmmn.group.GroupFactory;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.cmmn.util.WIPProperty;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.group.domain.Group;
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

import java.util.ArrayList;
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
    private UserRepository userRepository;

    private User getNormalUser() {
        User user = UserFactory.getNormalUser();
        return user;
    }

    private User getAdminUser() {
        User user = UserFactory.getAdminUser();
        return user;
    }

    private Group getGroup(User user) {
        Group group = GroupFactory.getGroup(user);
        return group;
    }

    private Group getGroup(String groupName, User user) {
        Group group = GroupFactory.getGroup(groupName, user);
        return group;
    }

    private void addGroup(String groupName, User user) {
        Group group = getGroup(groupName, user);
        groupRepository.save(group);
    }

    @DisplayName("그룹추가")
    @Test
    public void 그룹추가_Repository() throws Exception {

        //given
        User user = getNormalUser();
        User addUser = userRepository.save(user);
        Group group = getGroup(addUser);

        //when
        Group addGroup = groupRepository.save(group);

        //then
        assertThat(addGroup.getGroupName()).isEqualTo(group.getGroupName());
        assertThat(addGroup.getId()).isGreaterThan(0L);
    }


    @DisplayName("그룹수정")
    @Test
    public void 그룹수정_Repository() throws Exception {

        //given
        User user = getNormalUser();
        User addUser = userRepository.save(user);
        Group group = getGroup(addUser);
        Group addGroup = groupRepository.save(group);
        String updateGroupName = "테스트그룹_2";
        group.updateGroupName(updateGroupName);

        //when
        Group updateGroup = groupRepository.save(group);

        //then
        assertThat(updateGroup.getGroupName()).isEqualTo(updateGroupName);
        assertThat(updateGroup.getId()).isGreaterThan(0L);
    }

    @DisplayName("그룹삭제")
    @Test
    public void 그룹삭제_Repository() throws Exception {

        //given
        User user = getNormalUser();
        User addUser = userRepository.save(user);
        Group group = getGroup(addUser);
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
        User user = getNormalUser();
        userRepository.save(user);
        String groupName = "테스트그룹_";
        List<String> groupNames = new ArrayList<>();
        int size = 10;
        int offset = 0;
        int cnt = 10;

        for(int i=1;i<=cnt;i++)
        {
            groupNames.add(groupName+i);
            addGroup(groupName + i, user);
        }

        //when
        List<Group> groups = groupRepository.findAllByMaster(user.getId(), OrderType.ASC, size, offset);

        //then
        assertThat(groups.size()).isEqualTo(cnt);
        assertThat(groups).extracting(Group::getGroupName).containsAll(groupNames);
    }

}
