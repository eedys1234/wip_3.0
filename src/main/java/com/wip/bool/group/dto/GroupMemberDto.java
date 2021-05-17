package com.wip.bool.group.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.group.domain.GroupMember;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

public class GroupMemberDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GroupMemberSaveRequest {

        @JsonProperty(value = "group_id")
        @Positive
        private Long groupId;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GroupMemberResponse {

        @JsonProperty(value = "group_member_id")
        private Long groupMemberId;

        @JsonProperty(value = "user_id")
        private Long userId;

        public GroupMemberResponse(GroupMember groupMember) {
            this.groupMemberId = groupMember.getId();
            this.userId = groupMember.getUser().getId();
        }
    }
}
