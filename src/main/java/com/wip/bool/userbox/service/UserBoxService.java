package com.wip.bool.userbox.service;


import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
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

@Service
@RequiredArgsConstructor
@Transactional
public class UserBoxService {

    private final UserRepository userRepository;

    private final UserBoxRepository userBoxRepository;

    public Long addUserBox(Long userId, UserBoxDto.UserBoxSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        UserBox userBox = UserBox.createUserBox(user, requestDto.getUserBoxName());
        return userBoxRepository.save(userBox).getId();
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
    public List<UserBoxDto.UserBoxResponse> findAllByUserId(Long userId, String order, int size, int offset) {

        OrderType orderType = OrderType.valueOf(order);

        return userBoxRepository.findAll(userId, orderType, size, offset)
                .stream()
                .map(UserBoxDto.UserBoxResponse::new)
                .collect(Collectors.toList());
    }

}
