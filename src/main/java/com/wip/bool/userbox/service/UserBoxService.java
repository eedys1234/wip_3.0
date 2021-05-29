package com.wip.bool.userbox.service;


import com.wip.bool.cmmn.auth.Authority;
import com.wip.bool.cmmn.auth.Target;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.dept.domain.Dept;
import com.wip.bool.dept.domain.DeptRepository;
import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.group.domain.Group;
import com.wip.bool.group.domain.GroupMember;
import com.wip.bool.group.domain.GroupMemberRepository;
import com.wip.bool.group.domain.GroupRepository;
import com.wip.bool.rights.domain.Rights;
import com.wip.bool.rights.domain.RightsRepository;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.userbox.domain.UserBox;
import com.wip.bool.userbox.domain.UserBoxRepository;
import com.wip.bool.userbox.dto.UserBoxDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class UserBoxService {

    private final UserRepository userRepository;

    private final UserBoxRepository userBoxRepository;

    private final DeptRepository deptRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final GroupRepository groupRepository;
    
    private final RightsRepository rightRepository;

    public Long addUserBox(Long userId, UserBoxDto.UserBoxSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        UserBox userBox = UserBox.createUserBox(user, requestDto.getUserBoxName());
        Long id = userBoxRepository.save(userBox).getId();
        rightRepository.save(Rights.of(Target.USERBOX, id, Authority.USER, userId));

        return id;
    }

    public Long updateUserBox(Long userId, Long userBoxId, UserBoxDto.UserBoxUpdateRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role != Role.ROLE_ADMIN && role != Role.ROLE_NORMAL) {
            throw new AuthorizationException();
        }

        UserBox userBox = userBoxRepository.findById(userId, userBoxId, role)
                .orElseThrow(() -> new EntityNotFoundException(userBoxId, ErrorCode.NOT_FOUND_USER_BOX));

        userBox.updateUserBoxName(requestDto.getUserBoxName());
        return userBoxRepository.save(userBox).getId();
    }

    public Long deleteUserBox(Long userId, Long userBoxId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role != Role.ROLE_ADMIN && role != Role.ROLE_NORMAL) {
            throw new AuthorizationException();
        }

        UserBox userBox = userBoxRepository.findById(userId, userBoxId, role)
                .orElseThrow(() -> new EntityNotFoundException(userBoxId, ErrorCode.NOT_FOUND_USER_BOX));

        Rights right = rightRepository.findByTargetIdAndTarget(Target.USERBOX, userBox.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_RIGHT));

        rightRepository.delete(right);

        return userBoxRepository.delete(userBox);
    }

    @Transactional(readOnly = true)
    public List<UserBoxDto.UserBoxResponse> findAllByUser(Long userId, String order, int size, int offset) {

        OrderType orderType = OrderType.valueOf(order);

        return userBoxRepository.findAll(orderType, size, offset, userId);
    }

    @Transactional(readOnly = true)
    public List<UserBoxDto.UserBoxResponse> findAllByDept(Long userId, Long deptId, String order, int size, int offset) {

        User user = userRepository.deptByUser(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Dept dept = deptRepository.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException(deptId, ErrorCode.NOT_FOUND_DEPT));

        Dept user_dept = Optional.ofNullable(user.getDept())
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_DEPT));

        if(!user_dept.getId().equals(dept.getId())) {
            throw new AuthorizationException();
        }

        OrderType orderType = OrderType.valueOf(order);

        return userBoxRepository.findAll(orderType, size, offset, deptId);
    }

    @Transactional(readOnly = true)
    public List<UserBoxDto.UserBoxResponse> findAllByGroup(Long userId, String groupId, String order, int size, int offset) {

        User user = userRepository.deptByUser(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        List<Long> groupIds = Stream.of(groupId.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<GroupMember> groupMembers = groupMemberRepository.findAllByGroup(groupIds);

        //체킹
        if(!groupMembers.stream().allMatch(groupMember -> groupMember.getUser() != null && groupMember.getUser().getId().equals(user.getId()))) {
            throw new AuthorizationException();
        }
        
        OrderType orderType = OrderType.valueOf(order);

        return userBoxRepository.findAll(orderType, size, offset, groupIds);
    }

    @Transactional(readOnly = true)
    public List<UserBoxDto.UserBoxResponse> findAllByTotal(Long userId, String order, int size, int offset) {

        User user = userRepository.deptByUser(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        List<Group> groups = groupRepository.findAllByUser(userId);

        Long deptId = user.getDept().getId();

        List<Long> authorityIds = groups.stream().map(Group::getId).collect(Collectors.toList());
        authorityIds.add(userId);
        authorityIds.add(deptId);

        OrderType orderType = OrderType.valueOf(order);

        return userBoxRepository.findAll(orderType, size, offset, authorityIds);
    }
}
