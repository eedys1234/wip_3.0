package com.wip.bool.group.service;

import com.wip.bool.cmmn.auth.AuthExecutor;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.exception.excp.not_found.NotFoundGroupException;
import com.wip.bool.exception.excp.not_found.NotFoundUserException;
import com.wip.bool.group.domain.Group;
import com.wip.bool.group.domain.GroupMember;
import com.wip.bool.group.domain.GroupMemberRepository;
import com.wip.bool.group.domain.GroupRepository;
import com.wip.bool.group.dto.GroupDto;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long saveGroup(Long userId, GroupDto.GroupSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId));

        Group group = Group.createGroup(requestDto.getGroupName(), user);
        Long id = groupRepository.save(group).getId();

        GroupMember groupMember = GroupMember.createGroupMember(group, user);
        groupMemberRepository.save(groupMember);
        return id;
    }

    @Transactional
    public Long updateGroup(Long userId, Long groupId, GroupDto.GroupUpdateRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId));

        AuthExecutor<Long, Group> authExecutor = new AuthExecutor<>();

        Group group = authExecutor.execute(user, groupId,
                gid -> groupRepository.findById(gid)
                .orElseThrow(() -> new NotFoundGroupException(groupId)),
                (uid, gid) -> groupRepository.findById(uid, gid)
                .orElseThrow(() -> new NotFoundGroupException(groupId)));

        group.updateGroupName(requestDto.getGroupName());
        return groupRepository.save(group).getId();
    }

    @Transactional
    public Long deleteGroup(Long userId, Long groupId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId));

        AuthExecutor<Long, Group> authExecutor = new AuthExecutor<>();

        Group group = authExecutor.execute(user, groupId,
                gid -> groupRepository.findById(gid)
                .orElseThrow(() -> new NotFoundGroupException(groupId)),
                (uid, gid) -> groupRepository.findById(uid, gid)
                .orElseThrow(() -> new NotFoundGroupException(groupId)));

        return groupRepository.delete(group);
    }

    @Transactional(readOnly = true)
    public List<GroupDto.GroupResponse> findAllByMaster(Long userId, String order, int size, int offset) {

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId));

        OrderType orderType = OrderType.valueOf(order);
        return groupRepository.findAllByMaster(userId, orderType, size, offset)
                .stream()
                .map(GroupDto.GroupResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 내가 속해있는 그룹
     */
    @Transactional(readOnly = true)
    public List<GroupDto.GroupResponse> findAllByUser(Long userId, String order, int size, int offset) {

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId));

        OrderType orderType = OrderType.valueOf(order);
        return groupRepository.findAllByUser(userId, orderType, size, offset)
                .stream()
                .map(GroupDto.GroupResponse::new)
                .collect(Collectors.toList());
    }
}
