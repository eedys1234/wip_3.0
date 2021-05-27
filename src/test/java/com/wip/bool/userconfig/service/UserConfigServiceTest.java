package com.wip.bool.userconfig.service;

import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.cmmn.userconfig.UserConfigFactory;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserConfig;
import com.wip.bool.user.domain.UserConfigRepository;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.user.dto.UserConfigDto;
import com.wip.bool.user.service.UserConfigService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserConfigServiceTest {

    @InjectMocks
    private UserConfigService userConfigService;

    @Mock
    private UserConfigRepository userConfigRepository;

    @Mock
    private UserRepository userRepository;


    @DisplayName("사용자설정 수정")
    @Test
    public void 사용자설정_수정_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        UserConfig userConfig = UserConfigFactory.getUserConfig(1L);
        user.updateUserConfig(userConfig);
        UserConfigDto.UserConfigUpdateRequest requestDto = new UserConfigDto.UserConfigUpdateRequest();

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findJoinUserConfig(anyLong());
        Long id = userConfigService.updateUserConfig(user.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(userConfig.getId());

        //verify
        verify(userRepository, times(1)).findJoinUserConfig(anyLong());
    }

    @DisplayName("사용자설정 조회")
    @Test
    public void 사용자설정_조회_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        UserConfig userConfig = UserConfigFactory.getUserConfig(1L);
        user.updateUserConfig(userConfig);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findJoinUserConfig(anyLong());
        UserConfigDto.UserConfigResponse value = userConfigService.findUserConfig(user.getId());

        //then
        assertThat(value.getFontSize()).isEqualTo(userConfig.getFontSize());
        assertThat(value.getViewType()).isEqualTo(userConfig.getViewType());
        assertThat(value.getRecvAlaram()).isEqualTo(userConfig.getRecvAlaram());

        //verify
        verify(userRepository, times(1)).findJoinUserConfig(anyLong());
    }
}
