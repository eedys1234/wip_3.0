package com.wip.bool.app.controller;

import com.wip.bool.app.service.AppVersionService;
import com.wip.bool.app.dto.AppVersionDto;
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
@RequestMapping(value = "/api/v1/app")
@RequiredArgsConstructor
public class AppVersionController {

    private final AppVersionService appVersionService;

    @Permission(target = Role.ROLE_ADMIN)
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

    @Permission(target = Role.ROLE_ADMIN)
    @GetMapping(value = "/version")
    public ResponseEntity<AppVersionDto.AppVersionResponse> get(@RequestParam("name") String name) {
        return ResponseEntity.ok(appVersionService.get(name));
    }

    @Permission(target = Role.ROLE_ADMIN)
    @GetMapping(value = "/versions")
    public ResponseEntity<List<AppVersionDto.AppVersionResponse>> gets() {
        return ResponseEntity.ok(appVersionService.gets());
    }

    @Permission(target = Role.ROLE_ADMIN)
    @DeleteMapping(value = "/version/{appVersionId:[\\d]+}")
    public ResponseEntity<Long> delete(@PathVariable("appVersionId") Long appVersionId) {
        return ResponseEntity.ok(appVersionService.delete(appVersionId));
    }
}
