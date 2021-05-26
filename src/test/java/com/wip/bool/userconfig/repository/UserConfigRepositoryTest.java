package com.wip.bool.userconfig.repository;

import com.wip.bool.cmmn.userconfig.UserConfigFactory;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.user.domain.UserConfig;
import com.wip.bool.user.domain.UserConfigRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.wip.bool.cmmn.util.WIPProperty.TEST;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles(TEST)
@Transactional
public class UserConfigRepositoryTest {

    @Autowired
    private UserConfigRepository userConfigRepository;

    @DisplayName("사용자설정 수정")
    @Test
    public void 사용자설정_수정_Repository() throws Exception {

        //given
        UserConfig userConfig = UserConfigFactory.getUserConfig();
        userConfig.updateViewType("1");
        //when
        UserConfig save = userConfigRepository.save(userConfig);

        //then
        assertThat(save.getId()).isGreaterThan(0L);
        assertThat(save.getViewType()).isEqualTo("1");
    }

    @DisplayName("사용자설정 조회")
    @Test
    public void 사용자설정_조회_Repository () throws Exception{

        //given
        UserConfig userConfig = UserConfigFactory.getUserConfig();
        UserConfig save = userConfigRepository.save(userConfig);

        //when
        UserConfig findUserConfig = userConfigRepository.findById(save.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_USER));

        //then
        assertThat(findUserConfig.getFontSize()).isEqualTo(save.getFontSize());
        assertThat(findUserConfig.getRecvAlaram()).isEqualTo(save.getRecvAlaram());
        assertThat(findUserConfig.getViewType()).isEqualTo(save.getViewType());
    }
}
