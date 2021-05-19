package com.wip.bool.rights.controller;

import com.wip.bool.rights.dto.RightDto;
import com.wip.bool.rights.service.RightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class RightController {

    private final RightService rightService;

    @PostMapping(value = "/right")
    public ResponseEntity<Long> saveRight(@Valid @RequestBody RightDto.RightSaveRequest requestDto,
                                          Errors errors,
                                          UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = rightService.saveRight(requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/right/{rightId:[\\d]+}")
    public ResponseEntity<Long> deleteRight(@PathVariable Long rightId) {
        return ResponseEntity.ok(rightService.deleteRight(rightId));
    }
}
