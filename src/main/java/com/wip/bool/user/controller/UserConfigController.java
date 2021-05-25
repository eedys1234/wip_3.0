package com.wip.bool.user.controller;

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

    @PutMapping(value = "/user/config")
    public ResponseEntity<Long> updateUserConfig(@RequestBody @Valid UserConfigDto.UserConfigUpdateRequest requestDto,
                                                 @RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(userConfigService.updateUserConfig(userId, requestDto));
    }

    @GetMapping(value = "/user/config")
    public ResponseEntity<UserConfigDto.UserConfigResponse> findUserConfig(@RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(userConfigService.findUserConfig(userId));
    }
}


