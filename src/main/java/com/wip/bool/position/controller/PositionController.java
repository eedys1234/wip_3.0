package com.wip.bool.position.controller;

import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.position.service.PositionService;
import com.wip.bool.position.dto.PositionDto;
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
@RequestMapping(value = "/api/v1")
public class PositionController {

    private final PositionService positionService;

    @Permission(target = Role.ROLE_ADMIN)
    @PostMapping(value = "/position")
    public ResponseEntity<ApiResponse<Long>> savePosition(@RequestBody @Valid PositionDto.PositionSaveRequest requestDto,
                                                          @RequestHeader("userId") Long userId,
                                                          Errors errors,
                                                          UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = positionService.savePosition(userId, requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());
        return new ResponseEntity<>(ApiResponse.of(HttpStatus.CREATED.value(), id), httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_ADMIN)
    @PutMapping(value = "/position/{id}")
    public ResponseEntity<ApiResponse<Long>> updatePosition(@RequestBody @Valid PositionDto.PositionUpdateRequest requestDto,
                                                            @RequestHeader("userId") Long userId,
                                                            @PathVariable("id") Long positionId,
                                                            Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), positionService.updatePosition(userId, positionId, requestDto)));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/positions")
    public ResponseEntity<ApiResponse<List<PositionDto.PositionResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), positionService.findAll()));
    }

    @Permission(target = Role.ROLE_ADMIN)
    @DeleteMapping(value = "/position/{positionId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> deletePosition(@PathVariable Long positionId,
                                               @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), positionService.deletePosition(userId, positionId)));
    }
}
