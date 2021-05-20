package com.wip.bool.music.song.controller;

import com.wip.bool.music.song.service.SongDetailService;
import com.wip.bool.music.song.dto.SongDetailDto;
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

    @DeleteMapping(value = "/song-detail/{songDetailId:[\\d]+}")
    public ResponseEntity<Long> deleteSongDetail(@PathVariable Long songDetailId,
                                                 @RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(songDetailService.deleteSong(userId, songDetailId));
    }

    @GetMapping(value = "/song-details")
    public ResponseEntity<List<SongDetailDto.SongDetailSimpleResponse>> gets(
            @RequestParam("codeId") Long songMasterId,
            @RequestParam("order") String order,
            @RequestParam("sort") String sort,
            @RequestParam("size") int size,
            @RequestParam("offset") int offset) {

        return new ResponseEntity<>(songDetailService.gets(songMasterId, order, sort, size, offset), HttpStatus.OK);
    }

    @GetMapping(value = "/song-detail/{songDetailId:[\\d]+}")
    public ResponseEntity<SongDetailDto.SongDetailResponse> get(@PathVariable("songDetailId") Long songDetailId,
                                                                @RequestHeader("userId") Long userId) {

        return new ResponseEntity<>(songDetailService.get(songDetailId, userId), HttpStatus.OK);
    }

    @GetMapping(value = "/song-details/search")
    public ResponseEntity<List<SongDetailDto.SongDetailSimpleResponse>> search(
            @RequestParam("keyword") String keyword) {
        return new ResponseEntity<>(songDetailService.search(keyword), HttpStatus.OK);
    }
}
