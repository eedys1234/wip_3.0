package com.wip.bool.user.controller;

import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.user.service.UserService;
import com.wip.bool.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping(value = "/api/v1")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/user")
    public ResponseEntity<ApiResponse<Long>> join(@RequestBody @Valid UserDto.UserSaveRequest requestDto,
                                                 Errors errors, UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = userService.join(requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(ApiResponse.of(HttpStatus.CREATED.value(), id), httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/user/approval/{userId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> approve(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), userService.approve(userId)));
    }

    @PutMapping(value = "/user/{userId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> updateUser(@PathVariable Long userId,
                                                        @RequestBody @Valid UserDto.UserUpdateRequest requestDto,
                                                        Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), userService.update(userId, requestDto)));
    }

    @DeleteMapping(value = "/user/{userId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), userService.delete(userId)));
    }
}
