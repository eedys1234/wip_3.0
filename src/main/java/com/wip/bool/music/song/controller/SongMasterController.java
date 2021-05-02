package com.wip.bool.music.song.controller;

import com.wip.bool.music.song.service.SongMasterService;
import com.wip.bool.music.song.dto.SongMasterDto;
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
public class SongMasterController {

    private final SongMasterService songMasterService;

    @PostMapping(value = "/song-master")
    public ResponseEntity<Long> saveSongMaster(@Valid @RequestBody SongMasterDto.SongMasterSaveRequest requestDto,
                                               Errors errors,
                                               UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = songMasterService.save(requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/song-master/{songMasterId:[\\d]+}")
    public ResponseEntity<Long> deleteSongMaster(@PathVariable("songMasterId") Long songMasterId) {
        return new ResponseEntity<>(songMasterService.delete(songMasterId), HttpStatus.OK);
    }

    @GetMapping(value = "/song-masters")
    public ResponseEntity<List<SongMasterDto.SongMasterResponse>> getsSongMaster() {
        return new ResponseEntity<>(songMasterService.gets(), HttpStatus.OK);
    }
}