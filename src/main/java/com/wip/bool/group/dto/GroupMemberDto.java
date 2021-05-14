package com.wip.bool.group.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.group.domain.GroupMember;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

public class GroupMemberDto {

    @Getter
    @NoArgsConstructor
    public static class GroupMemberSaveRequest {

        @JsonProperty(value = "group_id")
        @Positive
        private Long groupId;
    }

    @Getter
    @NoArgsConstructor
    public static class GroupMemberResponse {

        @JsonProperty(value = "group_member_id")
        private Long groupMemberId;

        @JsonProperty(value = "group_id")
        private Long groupId;

        @JsonProperty(value = "group_name")
        private String groupName;

        @JsonProperty(value = "user_id")
        private Long userId;

        public GroupMemberResponse(GroupMember groupMember) {
            this.groupMemberId = groupMember.getId();
            this.groupId = groupMember.getGroup().getId();
            this.groupName = groupMember.getGroup().getGroupName();
            this.userId = groupMember.getUser().getId();
        }
    }
}
