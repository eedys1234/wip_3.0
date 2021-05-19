package com.wip.bool.app.service;

import com.wip.bool.app.domain.AppVersion;
import com.wip.bool.app.domain.AppVersionRepository;
import com.wip.bool.app.dto.AppVersionDto;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
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
                .orElseThrow(() -> new EntityNotFoundException(String.format("%s name = %s", ErrorCode.NOT_FOUND_APP.getMessage(), name), ErrorCode.NOT_FOUND_APP)));
    }

    @Transactional(readOnly = true)
    public List<AppVersionDto.AppVersionResponse> gets() {

        return appVersionRepository.findAll()
                .stream()
                .map(AppVersionDto.AppVersionResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long delete(Long appVersionId) {
        AppVersion appVersion = appVersionRepository.findById(appVersionId)
                .orElseThrow(() -> new EntityNotFoundException(appVersionId, ErrorCode.NOT_FOUND_APP));

        return appVersionRepository.delete(appVersion);
    }
}
