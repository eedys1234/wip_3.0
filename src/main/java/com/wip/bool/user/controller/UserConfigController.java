package com.wip.bool.user.controller;

import com.wip.bool.user.dto.UserConfigDto;
import com.wip.bool.user.service.UserConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class UserConfigController {

    private final UserConfigService userConfigService;

    @GetMapping(value = "/user/config")
    public ResponseEntity<UserConfigDto.UserConfigResponse> findUserConfig(@RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(userConfigService.findUserConfig(userId));
    }
}


