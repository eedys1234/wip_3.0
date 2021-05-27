package com.wip.bool.user.controller;

import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.security.Permission;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.dto.UserConfigDto;
import com.wip.bool.user.service.UserConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class UserConfigController {

    private final UserConfigService userConfigService;

    @Permission(target = Role.ROLE_NORMAL)
    @PutMapping(value = "/user/config")
    public ResponseEntity<ApiResponse<Long>> updateUserConfig(@RequestBody @Valid UserConfigDto.UserConfigUpdateRequest requestDto,
                                                             @RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.CREATED.value(), userConfigService.updateUserConfig(userId, requestDto)));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/user/config")
    public ResponseEntity<ApiResponse<UserConfigDto.UserConfigResponse>> findUserConfig(@RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), userConfigService.findUserConfig(userId)));
    }
}


