package com.wip.bool.group.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.group.domain.Group;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupDto {

    @Getter
    @NoArgsConstructor
    public static class GroupSaveRequest {

        @JsonProperty(value = "group_name")
        @NotBlank
        private String groupName;
    }

    @Getter
    @NoArgsConstructor
    public static class GroupUpdateRequest {

        @JsonProperty(value = "group_name")
        @NotBlank
        private String groupName;
    }

    @Getter
    @NoArgsConstructor
    public static class GroupResponse {

        @JsonProperty(value = "group_id")
        private Long groupId;

        @JsonProperty(value = "group_name")
        private String groupName;

        @JsonProperty(value = "group_master_id")
        private Long userId;

        @JsonProperty(value = "group_master_name")
        private String userName;

        @JsonProperty(value = "group_master_email")
        private String userEmail;

        @JsonProperty(value = "group_members")
        private List<GroupMemberDto.GroupMemberResponse> groupMember = new ArrayList<>();

        public GroupResponse(Group group) {
            this.groupId = group.getId();
            this.groupName = group.getGroupName();
            this.userId = group.getUser().getId();

            this.groupMember = group.getGroupMembers()
                    .stream()
                    .map(GroupMemberDto.GroupMemberResponse::new)
                    .collect(Collectors.toList());

            this.userName = group.getUser().getName();
            this.userEmail = group.getUser().getEmail();
        }
    }
}
