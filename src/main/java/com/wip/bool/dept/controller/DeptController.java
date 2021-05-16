package com.wip.bool.dept.controller;

import com.wip.bool.dept.service.DeptService;
import com.wip.bool.dept.dto.DeptDto;
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

    @PostMapping(value = "/dept")
    public ResponseEntity<Long> saveDept(@RequestBody @Valid DeptDto.DeptSaveRequest requestDto,
                                         @RequestHeader("userId") Long userId,
                                         Errors errors, UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = deptService.saveDept(userId, requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping(value = "/depts")
    public ResponseEntity<List<DeptDto.DeptResponse>> findAll() {
        return ResponseEntity.ok(deptService.findAll());
    }

    @PutMapping(value = "/dept/{deptId:[\\d]+}")
    public ResponseEntity<Long> updateDept(@PathVariable Long deptId,
                                           @RequestHeader("userId") Long userId,
                                           @RequestBody @Valid DeptDto.DeptUpdateRequest requestDto,
                                           Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(deptService.updateDept(userId, deptId, requestDto));
    }

    @DeleteMapping(value = "/dept/{deptId:[\\d]+}")
    public ResponseEntity<Long> deleteDept(@PathVariable Long deptId,
                                           @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(deptService.deleteDept(userId, deptId));
    }

}
