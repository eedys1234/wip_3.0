package com.wip.bool.group.controller;

import com.wip.bool.group.dto.GroupMemberDto;
import com.wip.bool.group.service.GroupMemberService;
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
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class GroupMemberController {

    private final GroupMemberService groupMemberService;

    @PostMapping(value = "/group/member")
    public ResponseEntity<Long> saveGroupMember(@RequestBody @Valid GroupMemberDto.GroupMemberSaveRequest requestDto,
                                                @RequestHeader("userId") Long userId,
                                                Errors errors, UriComponentsBuilder uriComponentsBuilder) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = groupMemberService.saveGroupMember(userId, requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/group/member/{groupMemberId:[\\d]+}")
    public ResponseEntity<Long> deleteGroupMember(@PathVariable Long groupMemberId,
                                                  @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(groupMemberService.deleteGroupMember(userId, groupMemberId));
    }

    @GetMapping(value = "/group/members/{groupId:[\\d]+}")
    public ResponseEntity<List<GroupMemberDto.GroupMemberResponse>> findAllByGroup(@PathVariable Long groupId) {

        return ResponseEntity.ok(groupMemberService.findAllByGroup(groupId));
    }
}
