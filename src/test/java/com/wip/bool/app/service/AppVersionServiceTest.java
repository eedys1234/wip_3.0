package com.wip.bool.app.service;

import com.wip.bool.app.domain.AppVersion;
import com.wip.bool.app.domain.AppVersionRepository;
import com.wip.bool.app.dto.AppVersionDto;
import com.wip.bool.cmmn.app.AppFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppVersionServiceTest {

    @InjectMocks
    private AppVersionService appVersionService;

    @Mock
    private AppVersionRepository appVersionRepository;

    @DisplayName("app 정보 추가")
    @Test
    public void app_정보_추가_Service() throws Exception {

        //given
        AppVersion appVersion = AppFactory.getAppVersion(1L);
        String name = "ILECTRON";
        String version = "1.0.0.0";

        AppVersionDto.AppVersionSaveRequest requestDto = new AppVersionDto.AppVersionSaveRequest();
        ReflectionTestUtils.setField(requestDto, "name", name);
        ReflectionTestUtils.setField(requestDto, "version", version);

        //when
        doReturn(appVersion).when(appVersionRepository).save(any(AppVersion.class));
        Long id = appVersionService.saveApp(requestDto);

        //then
        assertThat(id).isEqualTo(appVersion.getId());

        //verify
        verify(appVersionRepository, times(1)).save(any(AppVersion.class));
    }

    @DisplayName("app 정보 리스트 조회")
    @Test
    public void app_정보_리스트_조회_Service() throws Exception {

        //given
        List<AppVersion> appVersions = Arrays.asList(AppFactory.getAppVersion(1L));

        //when
        doReturn(appVersions).when(appVersionRepository).findAll();
        List<AppVersionDto.AppVersionResponse> values = appVersionService.gets();

        //then
        assertThat(values.size()).isEqualTo(appVersions.size());
        assertThat(values).extracting(AppVersionDto.AppVersionResponse::getVersion)
                .containsExactly("1.0.0.0");

        assertThat(values).extracting(AppVersionDto.AppVersionResponse::getName)
                .containsExactly("ILECTRON");

        //verfiy
        verify(appVersionRepository, times(1)).findAll();
    }

    @DisplayName("app 정보 조회")
    @Test
    public void app_정보_조회_Service() throws Exception {

        //given
        AppVersion appVersion = AppFactory.getAppVersion(1L);

        //when
        doReturn(Optional.ofNullable(appVersion)).when(appVersionRepository).findOne(any(String.class));
        AppVersionDto.AppVersionResponse values = appVersionService.get(appVersion.getName());

        //then
        assertThat(values.getName()).isEqualTo(appVersion.getName());
        assertThat(values.getVersion()).isEqualTo(appVersion.getVersion());

        //verify
        verify(appVersionRepository, times(1)).findOne(any(String.class));
    }

    @DisplayName("app 정보 삭제")
    @Test
    public void app_정보_삭제_Service() throws Exception {

        //given
        AppVersion appVersion = AppFactory.getAppVersion(1L);
        //when
        doReturn(Optional.ofNullable(appVersion)).when(appVersionRepository).findById(any(Long.class));
        doReturn(1L).when(appVersionRepository).delete(any(AppVersion.class));
        Long resValue = appVersionService.deleteApp(1L);

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(appVersionRepository, times(1)).findById(any(Long.class));
        verify(appVersionRepository, times(1)).delete(any(AppVersion.class));
    }
}
