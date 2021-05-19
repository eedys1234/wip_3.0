package com.wip.bool.group.service;

import com.wip.bool.cmmn.auth.AuthExecutor;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
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
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Group group = groupRepository.findById(requestDto.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException(requestDto.getGroupId(), ErrorCode.NOT_FOUND_GROUP));

        GroupMember groupMember = GroupMember.createGroupMember(group, user);
        return groupMemberRepository.save(groupMember).getId();
    }

    @Transactional
    public Long deleteGroupMember(Long userId, Long groupMemberId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        AuthExecutor<Long, GroupMember> authExecutor = new AuthExecutor<>();

        GroupMember groupMember = authExecutor.execute(user, groupMemberId,
                gid -> groupMemberRepository.findById(gid)
                        .orElseThrow(() -> new EntityNotFoundException(groupMemberId, ErrorCode.NOT_FOUND_GROUP)),
                (uid, gid) -> groupMemberRepository.findById(uid, gid)
                        .orElseThrow(() -> new EntityNotFoundException(groupMemberId, ErrorCode.NOT_FOUND_GROUP)));

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
