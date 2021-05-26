package com.wip.bool.app.controller;

import com.wip.bool.app.dto.AppVersionDto;
import com.wip.bool.app.service.AppVersionService;
import com.wip.bool.cmmn.ApiResponse;
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
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/app")
@RequiredArgsConstructor
public class AppVersionController {

    private final AppVersionService appVersionService;

    @Permission(target = Role.ROLE_ADMIN)
    @PostMapping(value = "/version")
    public ResponseEntity<ApiResponse<Long>> saveApp(@Valid @RequestBody AppVersionDto.AppVersionSaveRequest requestDto,
                                                    Errors errors,
                                                    UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = appVersionService.saveApp(requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(ApiResponse.of(HttpStatus.CREATED.value(), id), httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_ADMIN)
    @GetMapping(value = "/version")
    public ResponseEntity<ApiResponse<AppVersionDto.AppVersionResponse>> get(@RequestParam @NotNull String name) {
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), appVersionService.get(name)));
    }

    @Permission(target = Role.ROLE_ADMIN)
    @GetMapping(value = "/versions")
    public ResponseEntity<ApiResponse<List<AppVersionDto.AppVersionResponse>>> gets() {
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), appVersionService.gets()));
    }

    @Permission(target = Role.ROLE_ADMIN)
    @DeleteMapping(value = "/version/{appVersionId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> deleteApp(@PathVariable("appVersionId") Long appVersionId) {
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), appVersionService.deleteApp(appVersionId)));
    }
}
