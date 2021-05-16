package com.wip.bool.position.controller;

import com.wip.bool.position.service.PositionService;
import com.wip.bool.position.dto.PositionDto;
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

    @PostMapping(value = "/position")
    public ResponseEntity<Long> add(@RequestBody @Valid PositionDto.PositionSaveRequest requestDto,
                                    @RequestHeader("userId") Long userId,
                                    Errors errors,
                                    UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = positionService.savePosition(userId, requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());
        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/position/{id}")
    public ResponseEntity<Long> update(@RequestBody @Valid PositionDto.PositionUpdateRequest requestDto,
                                       @RequestHeader("userId") Long userId,
                                       @PathVariable("id") Long positionId,
                                       Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(positionService.updatePosition(userId, positionId, requestDto));
    }

    @GetMapping(value = "/positions")
    public ResponseEntity<List<PositionDto.PositionResponse>> findAll() {
        return ResponseEntity.ok(positionService.findAll());
    }

    @DeleteMapping(value = "/position/{positionId:[\\d]+}")
    public ResponseEntity<Long> deletePosition(@PathVariable Long positionId,
                                               @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(positionService.deletePosition(userId, positionId));
    }
}
