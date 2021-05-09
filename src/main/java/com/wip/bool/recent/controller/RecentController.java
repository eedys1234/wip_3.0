package com.wip.bool.recent.controller;

import com.wip.bool.recent.service.RecentService;
import com.wip.bool.recent.dto.RecentDto;
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

    @PostMapping(value = "/recent")
    public ResponseEntity<Long> save(@Valid @RequestBody RecentDto.RecentSaveRequest requestDto,
                                     @RequestHeader("userId") Long userId,
                                     Errors errors, UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = recentService.save(userId, requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping(value = "/recents")
    public ResponseEntity<List<RecentDto.RecentResponse>> gets(
            @RequestHeader("userId") Long userId,
            @RequestParam("size") int size,
            @RequestParam("offset") int offset) {

        return new ResponseEntity<>(recentService.gets(userId, size, offset), HttpStatus.OK);
    }
}
