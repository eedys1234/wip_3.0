package com.wip.bool.userbox.controller;

import com.wip.bool.security.Permission;
import com.wip.bool.user.domain.Role;
import com.wip.bool.userbox.service.UserBoxService;
import com.wip.bool.userbox.dto.UserBoxDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class UserBoxController {

    private final UserBoxService userBoxService;

    @Permission(target = Role.ROLE_NORMAL)
    @PostMapping(value = "/userbox")
    public ResponseEntity<Long> addUserBox(@Valid @RequestBody UserBoxDto.UserBoxSaveRequest requestDto,
                                     @RequestHeader("userId") Long userId,
                                     Errors errors,
                                     UriComponentsBuilder uriComponentsBuilder) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = userBoxService.addUserBox(userId, requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_NORMAL)
    @PutMapping(value = "/userbox/{userBoxId:[\\d]+}")
    public ResponseEntity<Long> updateUserBox(@PathVariable Long userBoxId,
                                       @RequestHeader("userId") Long userId,
                                       @Valid @RequestBody UserBoxDto.UserBoxUpdateRequest requestDto,
                                       Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(userBoxService.updateUserBox(userId, userBoxId, requestDto));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @DeleteMapping(value = "/userbox/{userBoxId:[\\d]+}")
    public ResponseEntity<Long> deleteUserBox(@PathVariable Long userBoxId,
                                       @RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(userBoxService.deleteUserBox(userId, userBoxId));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/user-userboxes")
    public ResponseEntity<List<UserBoxDto.UserBoxResponse>> findAllByUser(@RequestHeader("userId") Long userId,
                                                                 @RequestParam String order,
                                                                 @RequestParam int size,
                                                                 @RequestParam int offset) {

        return ResponseEntity.ok(userBoxService.findAllByUser(userId, order, size, offset));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/dept-userboxes")
    public ResponseEntity<List<UserBoxDto.UserBoxResponse>> findAllByDept(@RequestHeader("userId") Long userId,
                                                                          @RequestParam Long deptId,
                                                                          @RequestParam String order,
                                                                          @RequestParam int size,
                                                                          @RequestParam int offset) {

        return ResponseEntity.ok(userBoxService.findAllByDept(userId, deptId, order, size, offset));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/group-userboxes")
    public ResponseEntity<List<UserBoxDto.UserBoxResponse>> findAllByGroup(@RequestHeader("userId") Long userId,
                                                                           @RequestParam String groupId,
                                                                           @RequestParam String order,
                                                                           @RequestParam int size,
                                                                           @RequestParam int offset) {

        return ResponseEntity.ok(userBoxService.findAllByGroup(userId, groupId, order, size, offset));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "total-userboxes")
    public ResponseEntity<List<UserBoxDto.UserBoxResponse>> findAllByTotal(@RequestHeader("userId") Long userId,
                                                                           @RequestParam String order,
                                                                           @RequestParam int size,
                                                                           @RequestParam int offset) {

        return ResponseEntity.ok(userBoxService.findAllByTotal(userId, order, size, offset));
    }
}
