package com.wip.bool.music.sheet.controller;

import com.wip.bool.exception.excp.BusinessException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.music.sheet.service.SongSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/music")
@RequiredArgsConstructor
public class SongSheetController {

    private final SongSheetService songSheetService;

    @PostMapping(value = "/song-detail/{songDetailId:[\\d]+}/sheet")
    public ResponseEntity<Long> saveSongSheet(@PathVariable("songDetailId") Long songDetailId,
                                              @RequestHeader("userId") Long userId,
                                              MultipartHttpServletRequest multipartHttpServletRequest,
                                              UriComponentsBuilder uriComponentsBuilder) throws IOException {

        MultipartFile multipartFile = Optional.ofNullable(multipartHttpServletRequest)
                .map(multipart -> multipart.getFile("imagesFile"))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_SHEET));

        Long id = songSheetService.saveSongSheet(userId, songDetailId, multipartFile.getOriginalFilename(), multipartFile.getBytes());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }


    @DeleteMapping(value = "/song-detail/{songDetailId:[\\d]+}/sheet/{songSheetId:[\\d]+}")
    public ResponseEntity<Long> deleteSongSheet(@PathVariable("songDetailId") Long songDetailId,
                                                @PathVariable("songSheetId") Long songSheetId,
                                                @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(songSheetService.deleteSongSheet(userId, songSheetId));
    }
}
