package com.wip.bool.app.repository;

import com.wip.bool.app.domain.AppVersion;
import com.wip.bool.app.domain.AppVersionRepository;
import com.wip.bool.cmmn.app.AppFactory;
import com.wip.bool.configure.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestConfig.class)
@SpringBootTest
@ActiveProfiles(value = "test")
@Transactional
public class AppVersionRepositoryTest {

    @Autowired
    private AppVersionRepository appVersionRepository;

    @DisplayName("app 정보 추가")
    @Test
    public void app_정보_추가_Repository() throws Exception {

        //given
        AppVersion appVersion = AppFactory.getAppVersion();

        //when
        AppVersion addAppVersion = appVersionRepository.save(appVersion);

        //then
        assertThat(addAppVersion.getId()).isGreaterThan(0L);
    }

    @DisplayName("app 정보 삭제")
    @Test
    public void app_정보_삭제_Repository() throws Exception {

        //given
        AppVersion appVersion = AppFactory.getAppVersion();
        AppVersion addAppVersion = appVersionRepository.save(appVersion);

        //when
        Long resValue = appVersionRepository.delete(addAppVersion);

        //then
        assertThat(resValue).isEqualTo(1L);
    }
}
