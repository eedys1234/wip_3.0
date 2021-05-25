package com.wip.bool.user.controller;

import com.wip.bool.security.Permission;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.dto.UserConfigDto;
import com.wip.bool.user.service.UserConfigService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Long> updateUserConfig(@RequestBody @Valid UserConfigDto.UserConfigUpdateRequest requestDto,
                                                 @RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(userConfigService.updateUserConfig(userId, requestDto));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/user/config")
    public ResponseEntity<UserConfigDto.UserConfigResponse> findUserConfig(@RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(userConfigService.findUserConfig(userId));
    }
}


