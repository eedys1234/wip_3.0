package com.wip.bool.userbox.service;


import com.wip.bool.user.domain.User;
import com.wip.bool.userbox.domain.UserBox;
import com.wip.bool.userbox.domain.UserBoxRepository;
import com.wip.bool.user.domain.UserRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 없습니다."));

        UserBox userBox = UserBox.createUserBox(user, requestDto.getUserBoxName());
        return userBoxRepository.save(userBox).getId();
    }

    public Long updateUserBox(Long userBoxId, UserBoxDto.UserBoxUpdateRequest requestDto) {

        UserBox userBox = userBoxRepository.findById(userBoxId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 Box 정보가 없습니다. id = " + userBoxId));

        userBox.updateUserBoxName(requestDto.getUserBoxName());
        return userBoxRepository.save(userBox).getId();
    }

    public Long deleteUserBox(Long userBoxId) {
        UserBox userBox = userBoxRepository.findById(userBoxId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 Box 정보가 없습니다. id = " + userBoxId));

        return userBoxRepository.delete(userBox);
    }

    @Transactional(readOnly = true)
    public List<UserBoxDto.UserBoxResponse> findAllByUserId(Long userId) {
        return userBoxRepository.findAllByUserId(userId).stream()
                .map(UserBoxDto.UserBoxResponse::new)
                .collect(Collectors.toList());
    }

}
