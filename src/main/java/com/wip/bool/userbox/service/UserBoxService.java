package com.wip.bool.userbox.service;


import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.type.ShareType;
import com.wip.bool.exception.excp.AuthorizationException;
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

    public Long addUserBox(Long userId, UserBoxDto.UserBoxSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 없습니다."));

        ShareType shareType = ShareType.valueOf(requestDto.getShareType());
        UserBox userBox = UserBox.createUserBox(user, requestDto.getUserBoxName(), shareType);
        return userBoxRepository.save(userBox).getId();
    }

    public Long updateUserBox(Long userId, Long userBoxId, UserBoxDto.UserBoxUpdateRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 없습니다."));

        UserBox userBox = null;
        Role role = user.getRole();

        if(role == Role.ROLE_ADMIN) {
            userBox = userBoxRepository.findById(userBoxId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자 Box 정보가 없습니다. id = " + userBoxId));
        }
        else if(role == Role.ROLE_NORMAL) {

            userBox = userBoxRepository.findById(userId, userBoxId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자 Box 정보가 없습니다. id = " + userBoxId));
        }
        else {
            throw new AuthorizationException();
        }

        userBox.updateUserBoxName(requestDto.getUserBoxName());
        return userBoxRepository.save(userBox).getId();
    }

    public Long deleteUserBox(Long userId, Long userBoxId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 없습니다."));

        UserBox userBox = null;
        Role role = user.getRole();

        if(role == Role.ROLE_ADMIN) {
            userBox = userBoxRepository.findById(userBoxId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자 Box 정보가 없습니다. id = " + userBoxId));
        }
        else if(role == Role.ROLE_NORMAL) {
            userBox = userBoxRepository.findById(userId, userBoxId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자 Box 정보가 없습니다. id = " + userBoxId));
        }
        else {
            throw new AuthorizationException();
        }

        return userBoxRepository.delete(userBox);
    }

    @Transactional(readOnly = true)
    public List<UserBoxDto.UserBoxResponse> findAllByUserId(Long userId, String order, String share, int size, int offset) {

        OrderType orderType = OrderType.valueOf(order);

        List<ShareType> shareTypes = Stream.of(share.split(","))
                                    .map(type -> ShareType.valueOf(type))
                                    .collect(Collectors.toList());

        return userBoxRepository.findAll(userId, orderType, shareTypes, size, offset)
                .stream()
                .map(UserBoxDto.UserBoxResponse::new)
                .collect(Collectors.toList());
    }

}
