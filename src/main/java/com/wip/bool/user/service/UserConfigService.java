package com.wip.bool.user.service;

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

    public Long update(Long userId, UserConfigDto.UserConfigUpdateRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 존재하지 않습니다."));

        UserConfig userConfig = user.getUserConfig();

        userConfig.update(requestDto.getFontSize(), requestDto.getViewType(), requestDto.getRecvAlaram());
        userConfigRepository.save(userConfig);

        return userConfig.getId();
    }

    @Transactional(readOnly = true)
    public UserConfigDto.UserConfigResponse findOne(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 존재하지 않습니다."));

        return new UserConfigDto.UserConfigResponse(user.getUserConfig());
    }
}
