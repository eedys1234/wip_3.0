package com.wip.bool.userbox.controller;

import com.wip.bool.security.Permission;
import com.wip.bool.user.domain.Role;
import com.wip.bool.userbox.service.UserBoxSongService;
import com.wip.bool.userbox.dto.UserBoxSongDto;
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
@RequestMapping(value = "/api/v1/userbox")
@RequiredArgsConstructor
public class UserBoxSongController {

    private final UserBoxSongService userBoxSongService;

    @Permission(target = Role.ROLE_NORMAL)
    @PostMapping(value = "/{userBoxId:[\\d]+}/song")
    public ResponseEntity<Long> saveUserBoxSong(@Valid @RequestBody UserBoxSongDto.UserBoxSongSaveRequest requestDto,
                                     @PathVariable Long userBoxId,
                                     @RequestHeader("userId") Long userId,
                                     Errors errors,
                                     UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = userBoxSongService.saveUserBoxSong(userId, userBoxId, requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/{userBoxId:[\\d]+}/songs")
    public ResponseEntity<List<UserBoxSongDto.UserBoxSongResponse>> findAllUserBoxSong(@PathVariable Long userBoxId,
                                                                         @RequestHeader("userId") Long userId,
                                                                         @RequestParam("sort") String sort,
                                                                         @RequestParam("order") String order,
                                                                         @RequestParam("size") int size,
                                                                         @RequestParam("offset") int offset) {

        return ResponseEntity.ok(userBoxSongService.findAllUserBoxSong(userId, userBoxId, sort, order, size, offset));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @DeleteMapping(value = "/{userBoxId:[\\d]+}/song/{userBoxSongId:[\\d]+}")
    public ResponseEntity<Long> deleteUserBoxSong(@PathVariable Long userBoxId,
                                                  @PathVariable Long userBoxSongId,
                                                  @RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(userBoxSongService.deleteUserBoxSong(userId, userBoxId, userBoxSongId));
    }
}
