package com.wip.bool.group.controller;


import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.group.dto.GroupDto;
import com.wip.bool.group.service.GroupService;
import com.wip.bool.security.Permission;
import com.wip.bool.user.domain.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@Slf4j
public class GroupController {

    private final GroupService groupService;

    @Permission(target = Role.ROLE_ADMIN)
    @PostMapping(value = "/group")
    public ResponseEntity<ApiResponse<Long>> saveGroup(@RequestBody @Valid GroupDto.GroupSaveRequest requestDto,
                                                       @RequestHeader("userId") Long userId,
                                                       Errors errors,
                                                       UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = groupService.saveGroup(userId, requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(ApiResponse.of(HttpStatus.CREATED.value(), id), httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_ADMIN)
    @PutMapping(value = "/group/{groupId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> updateGroup(@PathVariable Long groupId,
                                                         @RequestBody @Valid GroupDto.GroupUpdateRequest requestDto,
                                                         @RequestHeader("userId") Long userId,
                                                         Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), groupService.updateGroup(userId, groupId, requestDto)));
    }

    @Permission(target = Role.ROLE_ADMIN)
    @DeleteMapping(value = "/group/{groupId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> deleteGroup(@PathVariable Long groupId,
                                                         @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),groupService.deleteGroup(userId, groupId)));
    }

    @Permission(target = Role.ROLE_ADMIN)
    @GetMapping(value = "/master/groups")
    public ResponseEntity<ApiResponse<List<GroupDto.GroupResponse>>> findAllByMaster(@RequestHeader("userId") Long userId,
                                                                                     @RequestParam String order,
                                                                                     @RequestParam int size,
                                                                                     @RequestParam int offset) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), groupService.findAllByMaster(userId, order, size, offset)));
    }

    @Permission(target = Role.ROLE_ADMIN)
    @GetMapping(value = "/user/groups")
    public ResponseEntity<ApiResponse<List<GroupDto.GroupResponse>>> findAllByUser(@RequestHeader("userId") Long userId,
                                                                                   @RequestParam String order,
                                                                                   @RequestParam int size,
                                                                                   @RequestParam int offset) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), groupService.findAllByUser(userId, order, size, offset)));
    }

}
