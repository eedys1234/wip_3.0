package com.wip.bool.music.mp3.controller;

import com.wip.bool.exception.excp.NotFoundFileException;
import com.wip.bool.music.mp3.service.SongMP3Service;
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

@RestController
@RequestMapping(value = "/api/v1/music")
@RequiredArgsConstructor
public class SongMP3Controller {

    private final SongMP3Service songMP3Service;

    @PostMapping(value = "/song-detail/{songDetailId:[\\d]+}/mp3")
    public ResponseEntity<Long> saveSongMP3(@PathVariable("songDetailId") Long songDetailId,
                                            MultipartHttpServletRequest multipartHttpServletRequest,
                                            UriComponentsBuilder uriComponentsBuilder) throws IOException, NotFoundFileException
    {

        final String NOT_FOUND_FILE_ERROR = "MP3 파일 null 오류";

        MultipartFile multipartFile = Optional.ofNullable(multipartHttpServletRequest)
                .map(multipart -> multipart.getFile("mp3File"))
                .orElseThrow(() -> new NotFoundFileException(NOT_FOUND_FILE_ERROR));

        Long id = songMP3Service.save(songDetailId, multipartFile.getOriginalFilename(), multipartFile.getBytes());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/song-detail/{songDetailId:[\\d]+}/mp3/{songMP3Id:[\\d+]}")
    public ResponseEntity<Long> deleteSongMP3(@PathVariable("songDetailId") Long songDetailId,
                                              @PathVariable("songMP3Id") Long songMP3Id) {

        return new ResponseEntity<>(songMP3Service.delete(songMP3Id), HttpStatus.OK);
    }

    @GetMapping(value = "/song-detail/{songDetailId:[\\d]+}/mp3/{songMP3Id:[\\d]+}")
    public ResponseEntity<Object> getMP3File(@PathVariable("songDetailId") Long songDetailId,
                                             @PathVariable("songMP3Id") Long songMP3Id) {

        Base64.Encoder encoder = Base64.getEncoder();
        return new ResponseEntity<>(encoder.encodeToString(songMP3Service.getFile(songMP3Id)), HttpStatus.OK);
    }
}
