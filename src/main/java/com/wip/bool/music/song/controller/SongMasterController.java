package com.wip.bool.music.song.controller;

import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.music.song.service.SongMasterService;
import com.wip.bool.music.song.dto.SongMasterDto;
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
public class SongMasterController {

    private final SongMasterService songMasterService;

    @Permission(target = Role.ROLE_ADMIN)
    @PostMapping(value = "/song-master")
    public ResponseEntity<ApiResponse<Long>> saveSongMaster(@Valid @RequestBody SongMasterDto.SongMasterSaveRequest requestDto,
                                                            @RequestHeader("userId") Long userId,
                                                            Errors errors,
                                                            UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = songMasterService.saveSongMaster(userId, requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(ApiResponse.of(HttpStatus.CREATED.value(), id), httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_ADMIN)
    @DeleteMapping(value = "/song-master/{songMasterId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> deleteSongMaster(@PathVariable Long songMasterId,
                                                              @RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.CREATED.value(), songMasterService.deleteSongMaster(userId, songMasterId)));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/song-masters")
    public ResponseEntity<ApiResponse<List<SongMasterDto.SongMasterResponse>>> getsSongMaster() {
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), songMasterService.getsSongMaster()));
    }
}
