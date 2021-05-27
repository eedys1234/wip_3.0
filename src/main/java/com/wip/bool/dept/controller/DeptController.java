package com.wip.bool.dept.controller;

import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.dept.service.DeptService;
import com.wip.bool.dept.dto.DeptDto;
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
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class DeptController {

    private final DeptService deptService;

    @Permission(target = Role.ROLE_ADMIN)
    @PostMapping(value = "/dept")
    public ResponseEntity<ApiResponse<Long>> saveDept(@RequestBody @Valid DeptDto.DeptSaveRequest requestDto,
                                                      @RequestHeader("userId") Long userId,
                                                      Errors errors,
                                                      UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = deptService.saveDept(userId, requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(ApiResponse.of(HttpStatus.CREATED.value(), id), httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/depts")
    public ResponseEntity<List<DeptDto.DeptResponse>> findAll() {
        return ResponseEntity.ok(deptService.findAll());
    }

    @Permission(target = Role.ROLE_ADMIN)
    @PutMapping(value = "/dept/{deptId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> updateDept(@PathVariable Long deptId,
                                                        @RequestHeader("userId") Long userId,
                                                        @RequestBody @Valid DeptDto.DeptUpdateRequest requestDto,
                                                        Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), deptService.updateDept(userId, deptId, requestDto)));
    }

    @Permission(target = Role.ROLE_ADMIN)
    @DeleteMapping(value = "/dept/{deptId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> deleteDept(@PathVariable Long deptId,
                                           @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), deptService.deleteDept(userId, deptId)));
    }

}
