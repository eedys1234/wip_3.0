package com.wip.bool.web.controller.app;

import com.wip.bool.service.app.AppVersionService;
import com.wip.bool.web.dto.app.AppVersionDto;
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
@RequestMapping(value = "/api/v1/app")
@RequiredArgsConstructor
public class AppVersionController {

    private final AppVersionService appVersionService;

    @PostMapping(value = "/version")
    public ResponseEntity<Long> save(@Valid @RequestBody AppVersionDto.AppVersionSaveRequest requestDto,
                                     Errors errors, UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = appVersionService.save(requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping(value = "/version")
    public ResponseEntity<AppVersionDto.AppVersionResponse> get(@RequestParam("name") String name) {
        return new ResponseEntity<>(appVersionService.get(name), HttpStatus.OK);
    }

    @GetMapping(value = "/versions")
    public ResponseEntity<List<AppVersionDto.AppVersionResponse>> gets() {
        return new ResponseEntity<>(appVersionService.gets(), HttpStatus.OK);
    }
}
