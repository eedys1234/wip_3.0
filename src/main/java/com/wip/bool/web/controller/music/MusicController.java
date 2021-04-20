package com.wip.bool.web.controller.music;

import com.wip.bool.exception.handler.NotFoundFileException;
import com.wip.bool.service.music.SongDetailService;
import com.wip.bool.service.music.SongMP3Service;
import com.wip.bool.service.music.SongMasterService;
import com.wip.bool.service.music.SongSheetService;
import com.wip.bool.web.dto.music.SongDetailDto;
import com.wip.bool.web.dto.music.SongMasterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

//import org.springframework.security.crypto.codec.Base64;

@RestController
@RequestMapping(value = "/api/v1/music")
@RequiredArgsConstructor
public class MusicController {

    private final SongMasterService songMasterService;
    private final SongDetailService songDetailService;
    private final SongSheetService songSheetService;
    private final SongMP3Service songMP3Service;

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

    @PutMapping(value = "/song-detail/{id}")
    public ResponseEntity<Long> updateSongDetail(@Valid @RequestBody SongDetailDto.SongDetailUpdateRequest requestDto,
                                       @PathVariable("id") Long songDetailId,
                                       Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(songDetailService.update(songDetailId, requestDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/song-detail/{id}")
    public ResponseEntity<Long> deleteSongDetail(@PathVariable("id") Long songDetailId) {
        return new ResponseEntity<>(songDetailService.delete(songDetailId), HttpStatus.OK);
    }

    @GetMapping(value = "/song-details")
    public ResponseEntity<List<SongDetailDto.SongDetailResponse>> gets(
            @Valid @RequestBody SongDetailDto.SongDetailsRequest requestDto,
            @RequestParam("size") int size,
            @RequestParam("page") int page) {

        return new ResponseEntity<>(songDetailService.gets(requestDto, size, page), HttpStatus.OK);
    }

    @GetMapping(value = "/song-detail/{songDetailId}")
    public ResponseEntity<SongDetailDto.SongDetailResponse> get(
            @PathVariable("songDetailId") Long songDetailId) {

        return new ResponseEntity<>(songDetailService.get(songDetailId), HttpStatus.OK);
    }

    @PostMapping(value = "/{songDetailId}/sheet")
    public ResponseEntity<Long> saveSongSheet(@PathVariable("songDetailId") Long songDetailId,
                                     MultipartHttpServletRequest multipartHttpServletRequest,
                                     UriComponentsBuilder uriComponentsBuilder) throws IOException, NotFoundFileException {

        final String NOT_FOUND_FILE_ERROR = "이미지 파일 null 오류";

        MultipartFile multipartFile = Optional.ofNullable(multipartHttpServletRequest)
                .map(multipart -> multipart.getFile("imageFiles"))
                .orElseThrow(() -> new NotFoundFileException(NOT_FOUND_FILE_ERROR));

        Long id = songSheetService.save(songDetailId, multipartFile.getBytes());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }


    @DeleteMapping(value = "/{songDetailId}/sheet/{songSheetId}")
    public ResponseEntity<Long> deleteSongSheet(@PathVariable("songDetailId") Long songDetailId,
                                       @PathVariable("songSheetId") Long songSheetId)
    {
        return new ResponseEntity<>(songSheetService.delete(songSheetId), HttpStatus.OK);
    }

    @PostMapping(value = "/{songDetailId}/mp3")
    public ResponseEntity<Long> saveSongMP3(@PathVariable("songDetailId") Long songDetailId,
                                     MultipartHttpServletRequest multipartHttpServletRequest,
                                     UriComponentsBuilder uriComponentsBuilder) throws IOException, NotFoundFileException
    {

        final String NOT_FOUND_FILE_ERROR = "MP3 파일 null 오류";

        MultipartFile multipartFile = Optional.ofNullable(multipartHttpServletRequest)
                .map(multipart -> multipart.getFile("imageFiles"))
                .orElseThrow(() -> new NotFoundFileException(NOT_FOUND_FILE_ERROR));

        Long id = songMP3Service.save(songDetailId, multipartFile.getBytes());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{songDetailId}/mp3/{songMP3Id}")
    public ResponseEntity<Long> deleteSongMP3(@PathVariable("songDetailId") Long songDetailId,
                                              @PathVariable("songMP3Id") Long songMP3Id) {

        return new ResponseEntity<>(songMP3Service.delete(songMP3Id), HttpStatus.OK);
    }

    @GetMapping(value = "/{songDetailId}/mp3/{songMP3Id}")
    public ResponseEntity<Object> getMP3File(@PathVariable("songDetailId") Long songDetailId,
                                             @PathVariable("songMP3Id") Long songMP3Id) {

        Base64.Encoder encoder = Base64.getEncoder();
        return new ResponseEntity<>(encoder.encodeToString(songMP3Service.getFile(songMP3Id)), HttpStatus.OK);
    }

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

    @DeleteMapping(value = "/song-master/{songMasterId}")
    public ResponseEntity<Long> deleteSongMaster(@PathVariable("songMasterId") Long songMasterId) {
        return new ResponseEntity<>(songMasterService.delete(songMasterId), HttpStatus.OK);
    }

    @GetMapping(value = "/song-masters")
    public ResponseEntity<List<SongMasterDto.SongMasterResponse>> getsSongMaster() {
        return new ResponseEntity<>(songMasterService.gets(), HttpStatus.OK);
    }


}
