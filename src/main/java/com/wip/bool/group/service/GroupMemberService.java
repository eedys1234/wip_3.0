package com.wip.bool.group.service;

import com.wip.bool.cmmn.auth.AuthExecutor;
import com.wip.bool.group.domain.Group;
import com.wip.bool.group.domain.GroupMember;
import com.wip.bool.group.domain.GroupMemberRepository;
import com.wip.bool.group.domain.GroupRepository;
import com.wip.bool.group.dto.GroupMemberDto;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long saveGroupMember(Long userId, GroupMemberDto.GroupMemberSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다. id =" + userId));

        Group group = groupRepository.findById(requestDto.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다. id = " + requestDto.getGroupId()));

        GroupMember groupMember = GroupMember.createGroupMember(group, user);
        return groupMemberRepository.save(groupMember).getId();
    }

    @Transactional
    public Long deleteGroupMember(Long userId, Long groupMemberId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다. id =" + userId));

        AuthExecutor<Long, GroupMember> authExecutor = new AuthExecutor<>();

        GroupMember groupMember = authExecutor.execute(user, groupMemberId,
                gid -> groupMemberRepository.findById(gid)
                        .orElseThrow(() -> new IllegalArgumentException("그룹에 속해있지 않습니다. id = " + groupMemberId)),
                (uid, gid) -> groupMemberRepository.findById(uid, gid)
                        .orElseThrow(() -> new IllegalArgumentException("그룹에 속해있지 않습니다. id = " + groupMemberId)));

        return groupMemberRepository.delete(groupMember);
    }

    @Transactional(readOnly = true)
    public List<GroupMemberDto.GroupMemberResponse> findAllByGroup(Long groupId) {
        return groupMemberRepository.findAllByGroup(groupId)
                .stream()
                .map(GroupMemberDto.GroupMemberResponse::new)
                .collect(Collectors.toList());
    }
}
