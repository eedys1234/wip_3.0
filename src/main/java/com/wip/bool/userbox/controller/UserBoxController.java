package com.wip.bool.userbox.controller;

import com.wip.bool.userbox.service.UserBoxService;
import com.wip.bool.userbox.dto.UserBoxDto;
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
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class UserBoxController {

    private final UserBoxService userBoxService;

    @PostMapping(value = "/userbox")
    public ResponseEntity<Long> save(@Valid @RequestBody UserBoxDto.UserBoxSaveRequest requestDto,
                                     @RequestHeader("userId") Long userId,
                                     Errors errors, UriComponentsBuilder uriComponentsBuilder) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = userBoxService.addUserBox(userId, requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/userbox/{userBoxId:[\\d]+}")
    public ResponseEntity<Long> update(@PathVariable("userBoxId") Long userBoxId,
                                       @RequestHeader("userId") Long userId,
                                       @Valid @RequestBody UserBoxDto.UserBoxUpdateRequest requestDto,
                                       Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(userBoxService.updateUserBox(userId, userBoxId, requestDto));
    }

    @DeleteMapping(value = "/userbox/{userBoxId:[\\d]+}")
    public ResponseEntity<Long> delete(@PathVariable("userBoxId") Long userBoxId,
                                       @RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(userBoxService.deleteUserBox(userId, userBoxId));
    }

    @GetMapping(value = "/userboxes")
    public ResponseEntity<List<UserBoxDto.UserBoxResponse>> gets(@RequestHeader("userId") Long userId,
                                                                 @RequestParam String share,
                                                                 @RequestParam String order,
                                                                 @RequestParam int size,
                                                                 @RequestParam int offset) {
        return ResponseEntity.ok(userBoxService.findAllByUserId(userId, order, share, size, offset));
    }
}
