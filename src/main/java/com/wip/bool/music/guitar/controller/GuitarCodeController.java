package com.wip.bool.music.guitar.controller;

import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.music.guitar.service.GuitarCodeService;
import com.wip.bool.music.guitar.dto.GuitarCodeDto;
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
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/music")
public class GuitarCodeController {

    private final GuitarCodeService guitarCodeService;

    @Permission(target = Role.ROLE_ADMIN)
    @PostMapping(value = "/guitar/code")
    public ResponseEntity<ApiResponse<Long>> saveGuitarCode(@Valid @RequestBody GuitarCodeDto.GuitarCodeSaveRequest requestDto,
                                                            @RequestHeader("userId") Long userId,
                                                            Errors errors,
                                                            UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = guitarCodeService.saveGuitarCode(userId, requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(ApiResponse.of(HttpStatus.CREATED.value(), id), httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/guitar/codes")
    public ResponseEntity<ApiResponse<List<GuitarCodeDto.GuitarCodeResponse>>> getGuitarCoeds() {
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), guitarCodeService.getGuitarCodes()));
    }

    @Permission(target = Role.ROLE_ADMIN)
    @DeleteMapping(value = "/guitar/code/{guitarCodeId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> deleteGuitarCode(@PathVariable Long guitarCodeId,
                                                              @RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), guitarCodeService.deleteGuitarCode(userId, guitarCodeId)));
    }

}
