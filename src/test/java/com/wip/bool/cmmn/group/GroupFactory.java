package com.wip.bool.cmmn.group;

import com.wip.bool.group.domain.Group;
import com.wip.bool.user.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GroupFactory {

    public static String[] groupNames = {
        "그룹A",
        "그룹B",
        "그룹C",
        "그룹D"
    };

    public static List<String> getGroupNames() {
        return Arrays.stream(groupNames).collect(Collectors.toList());
    }

    public static List<Group> getGroups(User user) {

        return Arrays.stream(groupNames)
                .map(groupName -> getGroup(groupName, user))
                .collect(Collectors.toList());
    }

    public static List<Group> getGroupsWithId(User user) {

        AtomicInteger index = new AtomicInteger(1);
        return Arrays.stream(groupNames)
                .map(groupName -> {
                    Group group = getGroup(groupName, user, index.longValue());
                    index.incrementAndGet();
                    return group;
                })
                .collect(Collectors.toList());
    }

    public static Group getGroup(User user) {
        String groupName = "그룹_A";
        Group group = getGroup(groupName, user);
        return group;
    }

    public static Group getGroup(User user, long id) {
        String groupName = "그룹_A";
        Group group = getGroup(groupName, user);
        ReflectionTestUtils.setField(group, "id", id);
        return group;
    }

    public static Group getGroup(String groupName, User user) {
        Group group = Group.createGroup(groupName, user);
        return group;
    }

    public static Group getGroup(String groupName, User user, long id) {
        Group group = Group.createGroup(groupName, user);
        ReflectionTestUtils.setField(group, "id", id);
        return group;
    }
}
