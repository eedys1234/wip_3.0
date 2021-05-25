package com.wip.bool.music.song.controller;

import com.wip.bool.music.song.service.SongDetailService;
import com.wip.bool.music.song.dto.SongDetailDto;
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
@RequestMapping(value = "/api/v1/music")
@RequiredArgsConstructor
public class SongDetailController {

    private final SongDetailService songDetailService;

    @Permission(target = Role.ROLE_ADMIN)
    @PostMapping(value = "/song-detail")
    public ResponseEntity<Long> saveSong(@Valid @RequestBody SongDetailDto.SongDetailSaveRequest requestDto,
                                         @RequestHeader("userId") Long userId,
                                         Errors errors,
                                         UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = songDetailService.saveSong(userId, requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_ADMIN)
    @PutMapping(value = "/song-detail/{songDetailId:[\\d]+}")
    public ResponseEntity<Long> updateSongDetail(@Valid @RequestBody SongDetailDto.SongDetailUpdateRequest requestDto,
                                                 @RequestHeader("userId") Long userId,
                                                 @PathVariable Long songDetailId,
                                                 Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(songDetailService.updateSong(userId, songDetailId, requestDto));
    }

    @Permission(target = Role.ROLE_ADMIN)
    @DeleteMapping(value = "/song-detail/{songDetailId:[\\d]+}")
    public ResponseEntity<Long> deleteSongDetail(@PathVariable Long songDetailId,
                                                 @RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(songDetailService.deleteSong(userId, songDetailId));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/song-details")
    public ResponseEntity<List<SongDetailDto.SongDetailSimpleResponse>> findAll(@RequestParam Long songMasterId,
                                                                        @RequestParam String order,
                                                                        @RequestParam String sort,
                                                                        @RequestParam int size,
                                                                        @RequestParam int offset) {

        return ResponseEntity.ok(songDetailService.findAll(songMasterId, order, sort, size, offset));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/song-detail/{songDetailId:[\\d]+}")
    public ResponseEntity<SongDetailDto.SongDetailResponse> findDetailOne(@PathVariable("songDetailId") Long songDetailId,
                                                                @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(songDetailService.findDetailOne(songDetailId, userId));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/song-details/search")
    public ResponseEntity<List<SongDetailDto.SongDetailSimpleResponse>> search(
            @RequestParam("keyword") String keyword) {
        return new ResponseEntity<>(songDetailService.search(keyword), HttpStatus.OK);
    }
}
