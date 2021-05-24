package com.wip.bool.music.mp3.controller;

import com.wip.bool.exception.excp.BusinessException;
import com.wip.bool.music.mp3.service.SongMP3Service;
import com.wip.bool.security.Permission;
import com.wip.bool.user.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import static com.wip.bool.exception.excp.ErrorCode.NOT_FOUND_MP3;

@RestController
@RequestMapping(value = "/api/v1/music")
@RequiredArgsConstructor
public class SongMP3Controller {

    private final SongMP3Service songMP3Service;

    @Permission(target = Role.ROLE_ADMIN)
    @PostMapping(value = "/song-detail/{songDetailId:[\\d]+}/mp3")
    public ResponseEntity<Long> saveSongMP3(@PathVariable Long songDetailId,
                                            @RequestHeader("userId") Long userId,
                                            MultipartHttpServletRequest multipartHttpServletRequest,
                                            UriComponentsBuilder uriComponentsBuilder) throws IOException
    {

        MultipartFile multipartFile = Optional.ofNullable(multipartHttpServletRequest)
                .map(multipart -> multipart.getFile("mp3File"))
                .orElseThrow(() -> new BusinessException(NOT_FOUND_MP3));

        Long id = songMP3Service.saveSongMP3(userId, songDetailId, multipartFile.getOriginalFilename(), multipartFile.getBytes());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_ADMIN)
    @DeleteMapping(value = "/song-detail/{songDetailId:[\\d]+}/mp3/{songMP3Id:[\\d+]}")
    public ResponseEntity<Long> deleteSongMP3(@PathVariable Long songDetailId,
                                              @PathVariable Long songMP3Id,
                                              @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(songMP3Service.deleteSongMP3(userId, songMP3Id));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/song-detail/{songDetailId:[\\d]+}/mp3/{songMP3Id:[\\d]+}")
    public ResponseEntity<Object> getMP3File(@PathVariable("songDetailId") Long songDetailId,
                                             @PathVariable("songMP3Id") Long songMP3Id) {

        Base64.Encoder encoder = Base64.getEncoder();
        return ResponseEntity.ok(encoder.encodeToString(songMP3Service.getFile(songMP3Id)));
    }
}
