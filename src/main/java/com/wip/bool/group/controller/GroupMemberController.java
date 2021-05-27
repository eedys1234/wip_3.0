package com.wip.bool.group.controller;

import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.group.dto.GroupMemberDto;
import com.wip.bool.group.service.GroupMemberService;
import com.wip.bool.security.Permission;
import com.wip.bool.user.domain.Role;
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

    @Permission(target = Role.ROLE_ADMIN)
    @PostMapping(value = "/group/member")
    public ResponseEntity<ApiResponse<Long>> saveGroupMember(@RequestBody @Valid GroupMemberDto.GroupMemberSaveRequest requestDto,
                                                             @RequestHeader("userId") Long userId,
                                                             Errors errors,
                                                             UriComponentsBuilder uriComponentsBuilder) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = groupMemberService.saveGroupMember(userId, requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(ApiResponse.of(HttpStatus.CREATED.value(),id), httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_ADMIN)
    @DeleteMapping(value = "/group/member/{groupMemberId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> deleteGroupMember(@PathVariable Long groupMemberId,
                                                               @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), groupMemberService.deleteGroupMember(userId, groupMemberId)));
    }

    @Permission(target = Role.ROLE_ADMIN)
    @GetMapping(value = "/group/{groupId:[\\d]+}/members")
    public ResponseEntity<ApiResponse<List<GroupMemberDto.GroupMemberResponse>>> findAllByGroup(@PathVariable Long groupId,
                                                                                                @RequestParam String order,
                                                                                                @RequestParam int size,
                                                                                                @RequestParam int offset) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), groupMemberService.findAllByGroup(groupId, order, size, offset)));
    }
}
