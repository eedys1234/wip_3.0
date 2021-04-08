package com.wip.bool.service.user;


import com.wip.bool.domain.user.User;
import com.wip.bool.domain.user.UserBox;
import com.wip.bool.domain.user.UserBoxRepository;
import com.wip.bool.domain.user.UserRepository;
import com.wip.bool.web.dto.user.UserDto;
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

    public Long addUserBox(Long userId, UserDto.UserBoxSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 없습니다."));

        UserBox userBox = UserBox.createUserBox(user, requestDto.getUserBoxName());
        return userBoxRepository.save(userBox).getId();
    }

    public Long updateUserBox(Long userBoxId, UserDto.UserBoxUpdateRequest requestDto) {

        UserBox userBox = userBoxRepository.findById(userBoxId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 Box 정보가 없습니다."));

        return userBoxRepository.save(userBox).getId();
    }

    public int deleteUserBox(Long userBoxId) {
        return userBoxRepository.delete(userBoxId);
    }

    @Transactional(readOnly = true)
    public List<UserDto.UserBoxResponse> findAllByUserId(Long userId) {
        return userBoxRepository.findAllByUserId(userId).stream()
                .map(UserDto.UserBoxResponse::new)
                .collect(Collectors.toList());
    }

}
