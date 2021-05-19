package com.wip.bool.group.domain;

import com.wip.bool.cmmn.util.BaseEntity;
import com.wip.bool.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "groups")
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "group")
    private List<GroupMember> groupMembers = new ArrayList<>();

    public static Group createGroup(String groupName, User user) {
        Group group = new Group();
        group.updateGroupName(groupName);
        group.setGroupMaster(user);
        return group;
    }

    public void setGroupMaster(User user) {
        this.user = user;
    }

    public void updateGroupName(String groupName) {
        if(!Objects.isNull(groupName)) {
            this.groupName = groupName;
        }
    }

}
