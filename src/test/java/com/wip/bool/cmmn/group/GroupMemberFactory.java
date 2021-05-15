package com.wip.bool.cmmn.group;

import com.wip.bool.group.domain.Group;
import com.wip.bool.group.domain.GroupMember;
import com.wip.bool.user.domain.User;

public class GroupMemberFactory {

    public static GroupMember getGroupMember(Group group, User user) {
        GroupMember groupMember = GroupMember.createGroupMember(group, user);
        return groupMember;
    }
}
