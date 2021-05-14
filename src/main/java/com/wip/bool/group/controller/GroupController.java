package com.wip.bool.group.controller;


import com.wip.bool.group.dto.GroupDto;
import com.wip.bool.group.service.GroupService;
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

    @PutMapping(value = "/group")
    public ResponseEntity<Long> saveGroup(@RequestBody @Valid GroupDto.GroupSaveRequest requestDto,
                                          @RequestHeader("userId") Long userId,
                                          Errors errors, UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = groupService.saveGroup(userId, requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/group/{groupId:[\\d]+}")
    public ResponseEntity<Long> updateGroup(@PathVariable Long groupId,
                                            @RequestBody @Valid GroupDto.GroupUpdateRequest requestDto,
                                            @RequestHeader("userId") Long userId,
                                            Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(groupService.updateGroup(userId, groupId, requestDto));
    }

    @DeleteMapping(value = "/group/{groupId:[\\d]+}")
    public ResponseEntity<Long> deleteGroup(@PathVariable Long groupId,
                                            @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(groupService.deleteGroup(userId, groupId));
    }

    @GetMapping(value = "/groups-master")
    public ResponseEntity<List<GroupDto.GroupResponse>> findAllByMaster(@RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(groupService.findAllByMaster(userId));
    }

    @GetMapping(value = "/groups-user")
    public ResponseEntity<List<GroupDto.GroupResponse>> findAllByUser(@RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(groupService.findAllByUser(userId));
    }

}
