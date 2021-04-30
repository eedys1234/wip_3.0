package com.wip.bool.app.service;

import com.wip.bool.app.domain.AppVersion;
import com.wip.bool.app.domain.AppVersionRepository;
import com.wip.bool.app.dto.AppVersionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppVersionService {

    private final AppVersionRepository appVersionRepository;

    @Transactional
    public Long save(AppVersionDto.AppVersionSaveRequest requestDto) {

        AppVersion appVersion = AppVersion.createAppVersion(requestDto.getName(), requestDto.getVersion());

        return appVersionRepository.save(appVersion).getId();
    }

    @Transactional(readOnly = true)
    public AppVersionDto.AppVersionResponse get(String name) {
        return new AppVersionDto.AppVersionResponse(appVersionRepository.findOne(name)
                .orElseThrow(() -> new IllegalArgumentException("앱 정보가 존재하지 않습니다. name = " + name)));
    }

    @Transactional(readOnly = true)
    public List<AppVersionDto.AppVersionResponse> gets() {

        return appVersionRepository.findAll()
                .stream()
                .map(AppVersionDto.AppVersionResponse::new)
                .collect(Collectors.toList());

    }
}
