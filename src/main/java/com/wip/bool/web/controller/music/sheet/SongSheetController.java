package com.wip.bool.web.controller.music.sheet;

import com.wip.bool.exception.excp.NotFoundFileException;
import com.wip.bool.service.music.sheet.SongSheetService;
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
                                              MultipartHttpServletRequest multipartHttpServletRequest,
                                              UriComponentsBuilder uriComponentsBuilder) throws IOException, NotFoundFileException {

        final String NOT_FOUND_FILE_ERROR = "이미지 파일 null 오류";

        MultipartFile multipartFile = Optional.ofNullable(multipartHttpServletRequest)
                .map(multipart -> multipart.getFile("imagesFile"))
                .orElseThrow(() -> new NotFoundFileException(NOT_FOUND_FILE_ERROR));

        Long id = songSheetService.save(songDetailId, multipartFile.getOriginalFilename(), multipartFile.getBytes());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }


    @DeleteMapping(value = "/song-detail/{songDetailId:[\\d]+}/sheet/{songSheetId:[\\d]+}")
    public ResponseEntity<Long> deleteSongSheet(@PathVariable("songDetailId") Long songDetailId,
                                                @PathVariable("songSheetId") Long songSheetId)
    {
        return new ResponseEntity<>(songSheetService.delete(songSheetId), HttpStatus.OK);
    }
}
