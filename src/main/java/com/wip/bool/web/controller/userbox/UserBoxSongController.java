package com.wip.bool.web.controller.userbox;

import com.wip.bool.service.userbox.UserBoxSongService;
import com.wip.bool.web.dto.userbox.UserBoxSongDto;
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
public class UserBoxSongController {

    private final UserBoxSongService userBoxSongService;

    @PostMapping(value = "/userbox/song")
    public ResponseEntity<Long> save(@Valid @RequestBody UserBoxSongDto.UserBoxSongSaveRequest requestDto,
                                     Errors errors, UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = userBoxSongService.save(requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping(value = "/userbox/songs")
    public ResponseEntity<List<UserBoxSongDto.UserBoxSongResponse>> gets(@RequestHeader("userId") Long userId,
                                                                         @RequestParam("sort") String sort,
                                                                         @RequestParam("order") String order,
                                                                         @RequestParam("size") int size,
                                                                         @RequestParam("offset") int offset) {

        return new ResponseEntity<>(userBoxSongService.gets(userId, sort, order, size, offset), HttpStatus.OK);
    }

    @DeleteMapping(value = "userbox/song/{userBoxSongId:[\\d]+}")
    public ResponseEntity<Long> delete(@PathVariable("userBoxSongId") Long userBoxSongId) {
        return new ResponseEntity<>(userBoxSongService.delete(userBoxSongId), HttpStatus.OK);
    }
}
