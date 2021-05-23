package com.wip.bool.cmmn.group;

import com.wip.bool.group.domain.Group;
import com.wip.bool.group.domain.GroupMember;
import com.wip.bool.user.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GroupMemberFactory {

    public static List<GroupMember> getGroupMembers(Group group, List<User> users) {
        return users.stream()
                .map(user -> getGroupMember(group, user))
                .collect(Collectors.toList());
    }

    public static List<GroupMember> getGroupMembersWithId(Group group, List<User> users) {
        AtomicInteger index = new AtomicInteger(0);
        return users.stream()
                .map(user -> getGroupMember(group, user, index.incrementAndGet()))
                .collect(Collectors.toList());
    }

    public static GroupMember getGroupMember(Group group, User user) {
        GroupMember groupMember = GroupMember.createGroupMember(group, user);
        return groupMember;
    }

    public static GroupMember getGroupMember(Group group, User user, long id) {
        GroupMember groupMember = GroupMember.createGroupMember(group, user);
        ReflectionTestUtils.setField(groupMember, "id", id);
        return groupMember;
    }
}
