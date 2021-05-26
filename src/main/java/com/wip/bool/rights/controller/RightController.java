package com.wip.bool.rights.controller;

import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.rights.dto.RightDto;
import com.wip.bool.rights.service.RightService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class RightController {

    private final RightService rightService;

    @Permission(target = Role.ROLE_NORMAL)
    @PostMapping(value = "/right")
    public ResponseEntity<ApiResponse<Long>> saveRight(@Valid @RequestBody RightDto.RightSaveRequest requestDto,
                                                      Errors errors,
                                                      UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = rightService.saveRight(requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(ApiResponse.of(HttpStatus.CREATED.value(), id), httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_NORMAL)
    @DeleteMapping(value = "/right/{rightId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> deleteRight(@PathVariable Long rightId,
                                                         @RequestParam("right_type") String rightType) {
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), rightService.deleteRight(rightId, rightType)));
    }
}
