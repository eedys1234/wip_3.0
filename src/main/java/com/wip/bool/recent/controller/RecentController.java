package com.wip.bool.recent.controller;

import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.recent.service.RecentService;
import com.wip.bool.recent.dto.RecentDto;
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
public class RecentController {

    private final RecentService recentService;

    @Permission(target = Role.ROLE_NORMAL)
    @PostMapping(value = "/recent")
    public ResponseEntity<ApiResponse<Long>> saveRecent(@Valid @RequestBody RecentDto.RecentSaveRequest requestDto,
                                                        @RequestHeader("userId") Long userId,
                                                        Errors errors,
                                                        UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = recentService.saveRecent(userId, requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(ApiResponse.of(HttpStatus.CREATED.value(),id), httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/recents")
    public ResponseEntity<ApiResponse<List<RecentDto.RecentResponse>>> findAll(@RequestHeader("userId") Long userId,
                                                                               @RequestParam("size") int size,
                                                                               @RequestParam("offset") int offset) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), recentService.findAll(userId, size, offset)));
    }
}
