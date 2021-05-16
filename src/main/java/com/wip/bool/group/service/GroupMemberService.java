package com.wip.bool.group.service;

import com.wip.bool.cmmn.auth.AuthExecutor;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.exception.excp.not_found.NotFoundGroupException;
import com.wip.bool.exception.excp.not_found.NotFoundGroupMemberException;
import com.wip.bool.exception.excp.not_found.NotFoundUserException;
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
                .orElseThrow(() -> new NotFoundUserException(userId));

        Group group = groupRepository.findById(requestDto.getGroupId())
                .orElseThrow(() -> new NotFoundGroupException(requestDto.getGroupId()));

        GroupMember groupMember = GroupMember.createGroupMember(group, user);
        return groupMemberRepository.save(groupMember).getId();
    }

    @Transactional
    public Long deleteGroupMember(Long userId, Long groupMemberId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId));

        AuthExecutor<Long, GroupMember> authExecutor = new AuthExecutor<>();

        GroupMember groupMember = authExecutor.execute(user, groupMemberId,
                gid -> groupMemberRepository.findById(gid)
                        .orElseThrow(() -> new NotFoundGroupMemberException(groupMemberId)),
                (uid, gid) -> groupMemberRepository.findById(uid, gid)
                        .orElseThrow(() -> new NotFoundGroupMemberException(groupMemberId)));

        return groupMemberRepository.delete(groupMember);
    }

    @Transactional(readOnly = true)
    public List<GroupMemberDto.GroupMemberResponse> findAllByGroup(Long groupId, String order, int size, int offset) {

        OrderType orderType = OrderType.valueOf(order);
        return groupMemberRepository.findAllByGroup(groupId, orderType, size, offset)
                .stream()
                .map(GroupMemberDto.GroupMemberResponse::new)
                .collect(Collectors.toList());
    }
}
