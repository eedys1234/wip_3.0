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
import com.wip.bool.right.domain.Right;
import com.wip.bool.right.domain.RightRepository;
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
    
    private final RightRepository rightRepository;

    public Long addUserBox(Long userId, UserBoxDto.UserBoxSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        UserBox userBox = UserBox.createUserBox(user, requestDto.getUserBoxName());
        Long id = userBoxRepository.save(userBox).getId();
        rightRepository.save(Right.of(Target.USERBOX, id, Authority.USER, userId));

        return id;
    }

    public Long updateUserBox(Long userId, Long userBoxId, UserBoxDto.UserBoxUpdateRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        UserBox userBox = null;
        Role role = user.getRole();

        if(role == Role.ROLE_ADMIN) {
            userBox = userBoxRepository.findById(userBoxId)
                    .orElseThrow(() -> new EntityNotFoundException(userBoxId, ErrorCode.NOT_FOUND_USER_BOX));
        }
        else if(role == Role.ROLE_NORMAL) {

            userBox = userBoxRepository.findById(userId, userBoxId)
                    .orElseThrow(() -> new EntityNotFoundException(userBoxId, ErrorCode.NOT_FOUND_USER_BOX));
        }
        else {
            throw new AuthorizationException();
        }

        userBox.updateUserBoxName(requestDto.getUserBoxName());
        return userBoxRepository.save(userBox).getId();
    }

    public Long deleteUserBox(Long userId, Long userBoxId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        UserBox userBox = null;
        Role role = user.getRole();

        if(role == Role.ROLE_ADMIN) {
            userBox = userBoxRepository.findById(userBoxId)
                    .orElseThrow(() -> new EntityNotFoundException(userBoxId, ErrorCode.NOT_FOUND_USER_BOX));
        }
        else if(role == Role.ROLE_NORMAL) {
            userBox = userBoxRepository.findById(userId, userBoxId)
                    .orElseThrow(() -> new EntityNotFoundException(userBoxId, ErrorCode.NOT_FOUND_USER_BOX));
        }
        else {
            throw new AuthorizationException();
        }

        return userBoxRepository.delete(userBox);
    }

    @Transactional(readOnly = true)
    public List<UserBoxDto.UserBoxResponse> findAllByUser(Long userId, String order, int size, int offset) {

        OrderType orderType = OrderType.valueOf(order);

        return userBoxRepository.findAll(orderType, size, offset, userId)
                .stream()
                .map(UserBoxDto.UserBoxResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserBoxDto.UserBoxResponse> findAllByDept(Long userId, Long deptId, String order, int size, int offset) {

        User user = userRepository.deptByUser(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Dept dept = deptRepository.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException(deptId, ErrorCode.NOT_FOUND_DEPT));

        if(user.getDept().getId() != dept.getId()) {
            throw new AuthorizationException();
        }

        OrderType orderType = OrderType.valueOf(order);

        return userBoxRepository.findAll(orderType, size, offset, deptId)
                .stream()
                .map(UserBoxDto.UserBoxResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserBoxDto.UserBoxResponse> findALlByGroup(Long userId, String groupId, String order, int size, int offset) {

        User user = userRepository.deptByUser(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        List<Long> groupIds = Stream.of(groupId.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<GroupMember> groupMembers = groupMemberRepository.findAllByGroup(groupIds);

        //체킹
        if(!groupMembers.stream().allMatch(groupMember -> groupMember.getUser().getId() == user.getId())) {
            throw new AuthorizationException();
        }
        
        OrderType orderType = OrderType.valueOf(order);

        return userBoxRepository.findAll(orderType, size, offset, groupIds)
                .stream()
                .map(UserBoxDto.UserBoxResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserBoxDto.UserBoxResponse> findALlByTotal(Long userId, String order, int size, int offset) {

        User user = userRepository.deptByUser(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        List<Group> groups = groupRepository.findAllByUser(userId);

        Long deptId = user.getDept().getId();

        List<Long> authorityIds = groups.stream().map(Group::getId).collect(Collectors.toList());
        authorityIds.add(userId);
        authorityIds.add(deptId);

        OrderType orderType = OrderType.valueOf(order);

        return userBoxRepository.findAll(orderType, size, offset, authorityIds)
                .stream()
                .map(UserBoxDto.UserBoxResponse::new)
                .collect(Collectors.toList());
    }
}
