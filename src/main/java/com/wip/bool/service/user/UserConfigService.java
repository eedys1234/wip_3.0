package com.wip.bool.service.user;

import com.wip.bool.domain.user.User;
import com.wip.bool.domain.user.UserConfig;
import com.wip.bool.domain.user.UserConfigRepository;
import com.wip.bool.domain.user.UserRepository;
import com.wip.bool.web.dto.user.UserConfigDto;
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
    public UserConfig findOne(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 존재하지 않습니다."));

        return user.getUserConfig();
    }
}
