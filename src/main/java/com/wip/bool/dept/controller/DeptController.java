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
    public ResponseEntity<Long> add(@RequestBody @Valid DeptDto.DeptSaveRequest requestDto
                                    , Errors errors, UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = deptService.add(requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping(value = "/depts")
    public ResponseEntity<List<DeptDto.DeptResponse>> findAll() {
        return new ResponseEntity<>(deptService.findAll(), HttpStatus.OK);
    }

    @PutMapping(value = "/dept/{id}")
    public ResponseEntity<Long> update(@PathVariable("id") Long deptId,
                                       @RequestBody @Valid DeptDto.DeptUpdateRequest requestDto,
                                       Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(deptService.update(deptId, requestDto), HttpStatus.OK);
    }
}
