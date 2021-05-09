package com.wip.bool.user.controller;

import com.wip.bool.user.service.UserConfigService;
import com.wip.bool.user.dto.UserConfigDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class UserConfigController {

    private final UserConfigService userConfigService;

    public ResponseEntity<UserConfigDto.UserConfigResponse> get(@RequestHeader("userId") Long userId) {
        return new ResponseEntity<>(userConfigService.findOne(userId), HttpStatus.OK);
    }
}


