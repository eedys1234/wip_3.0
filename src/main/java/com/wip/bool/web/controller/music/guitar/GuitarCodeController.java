package com.wip.bool.web.controller.music.guitar;

import com.wip.bool.service.music.guitar.GuitarCodeService;
import com.wip.bool.web.dto.music.guitar.GuitarCodeDto;
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
@RequestMapping(value = "/api/v1/music/guitar")
public class GuitarCodeController {

    private final GuitarCodeService guitarCodeService;

    @PostMapping(value = "/guitar/code")
    public ResponseEntity<Long> save(@Valid @RequestBody GuitarCodeDto.GuitarCodeSaveRequest requestDto,
                                     Errors errors, UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = guitarCodeService.save(requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping(value = "/guitar/codes")
    public ResponseEntity<List<GuitarCodeDto.GuitarCodeResponse>> gets() {
        return new ResponseEntity<>(guitarCodeService.gets(), HttpStatus.OK);
    }

}
