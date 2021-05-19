package com.wip.bool.cmmn.group;

import com.wip.bool.group.domain.Group;
import com.wip.bool.user.domain.User;

public class GroupFactory {

    public static Group getGroup(User user) {
        String groupName = "테스트그룹_1";
        Group group = Group.createGroup(groupName, user);
        return group;
    }

    public static Group getGroup(String groupName, User user) {
        Group group = Group.createGroup(groupName, user);
        return group;
    }
}
