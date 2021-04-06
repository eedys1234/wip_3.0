package com.wip.bool.web.controller.position;

import com.wip.bool.service.position.PositionService;
import com.wip.bool.web.dto.position.PositionDto;
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
                                    Errors erorrs, UriComponentsBuilder uriComponentsBuilder) {

        if(erorrs.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = positionService.add(requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/position/{id}")
    public ResponseEntity<Long> update(@RequestBody @Valid PositionDto.PositionUpdateRequest requestDto,
                                       @PathVariable("id") Long positionId,
                                       Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(positionService.update(positionId, requestDto), HttpStatus.OK);
    }

    @GetMapping(value = "/positions")
    public ResponseEntity<List<PositionDto.PositionResponse>> findAll() {
        return new ResponseEntity<>(positionService.findAll(), HttpStatus.OK);
    }
}
