package com.wip.bool.user.service;

import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserConfig;
import com.wip.bool.user.domain.UserConfigRepository;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.user.dto.UserConfigDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserConfigService {

    private final UserRepository userRepository;

    private final UserConfigRepository userConfigRepository;

    @Transactional
    public Long updateUserConfig(Long userId, UserConfigDto.UserConfigUpdateRequest requestDto) {

        User user = userRepository.findJoinUserConfig(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        UserConfig userConfig = user.getUserConfig();

        userConfig.update(requestDto.getFontSize(), requestDto.getViewType(), requestDto.getRecvAlaram());
        return userConfig.getId();
    }

    @Transactional(readOnly = true)
    public UserConfigDto.UserConfigResponse findUserConfig(Long userId) {

        User user = userRepository.findJoinUserConfig(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        return new UserConfigDto.UserConfigResponse(user.getUserConfig());
    }
}
