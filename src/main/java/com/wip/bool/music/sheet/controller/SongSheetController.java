package com.wip.bool.music.sheet.controller;

import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.exception.excp.BusinessException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.music.sheet.service.SongSheetService;
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
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/music")
@RequiredArgsConstructor
public class SongSheetController {

    private final SongSheetService songSheetService;

    @Permission(target = Role.ROLE_ADMIN)
    @PostMapping(value = "/song-detail/{songDetailId:[\\d]+}/sheet")
    public ResponseEntity<ApiResponse<Long>> saveSongSheet(@PathVariable("songDetailId") Long songDetailId,
                                                           @RequestHeader("userId") Long userId,
                                                           MultipartHttpServletRequest multipartHttpServletRequest,
                                                           UriComponentsBuilder uriComponentsBuilder) throws IOException {

        MultipartFile multipartFile = Optional.ofNullable(multipartHttpServletRequest)
                .map(multipart -> multipart.getFile("imagesFile"))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_SHEET));

        Long id = songSheetService.saveSongSheet(userId, songDetailId, multipartFile.getOriginalFilename(), multipartFile.getBytes());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(ApiResponse.of(HttpStatus.CREATED.value(), id), httpHeaders, HttpStatus.CREATED);
    }


    @Permission(target = Role.ROLE_ADMIN)
    @DeleteMapping(value = "/song-detail/{songDetailId:[\\d]+}/sheet/{songSheetId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> deleteSongSheet(@PathVariable("songDetailId") Long songDetailId,
                                                @PathVariable("songSheetId") Long songSheetId,
                                                @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), songSheetService.deleteSongSheet(userId, songSheetId)));
    }
}
