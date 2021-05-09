package com.wip.bool.app.repository;

import com.wip.bool.app.domain.AppVersion;
import com.wip.bool.app.domain.AppVersionRepository;
import com.wip.bool.configure.TestConfig;
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

    private AppVersion getAppVersion() {

        String name = "ILECTRON";
        String version = "1.0.0.0";
        AppVersion appVersion = AppVersion.createAppVersion(name, version);
        return appVersion;
    }

    @Test
    public void app_정보_추가_Repository() throws Exception {

        //given
        AppVersion appVersion = getAppVersion();

        //when
        AppVersion addAppVersion = appVersionRepository.save(appVersion);

        //then
        assertThat(addAppVersion.getId()).isGreaterThan(0L);
    }

    @Test
    public void app_정보_삭제_Repository() throws Exception {

        //given
        AppVersion appVersion = getAppVersion();
        AppVersion addAppVersion = appVersionRepository.save(appVersion);

        //when
        Long resValue = appVersionRepository.delete(addAppVersion);

        //then
        assertThat(resValue).isEqualTo(1L);
    }
}
