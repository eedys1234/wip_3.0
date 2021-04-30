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
    public ResponseEntity<Long> saveSongDetail(@Valid @RequestBody SongDetailDto.SongDetailSaveRequest requestDto,
                                               Errors errors, UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = songDetailService.save(requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/song-detail/{id:[\\d]+}")
    public ResponseEntity<Long> updateSongDetail(@Valid @RequestBody SongDetailDto.SongDetailUpdateRequest requestDto,
                                                 @PathVariable("id") Long songDetailId,
                                                 Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(songDetailService.update(songDetailId, requestDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/song-detail/{id:[\\d]+}")
    public ResponseEntity<Long> deleteSongDetail(@PathVariable("id") Long songDetailId) {
        return new ResponseEntity<>(songDetailService.delete(songDetailId), HttpStatus.OK);
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
