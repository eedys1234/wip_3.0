package com.wip.bool.web.controller.user;

//import com.wip.bool.service.user.UserService;

import com.wip.bool.service.user.UserService;
import com.wip.bool.web.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping(value = "/api/v1")
public class UserController {

    private final UserService userService;

    @PutMapping(value = "/user")
    public ResponseEntity<Long> join(@RequestBody @Valid UserDto.UserSaveRequest requestDto,
                                     Errors errors, UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = userService.join(requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @PostMapping(value = "/user/wip-login")
    public ResponseEntity<Long> login(@RequestBody @Valid UserDto.UserLoginRequest requestDto,
                                      Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long resValue =  userService.login(requestDto);

        return new ResponseEntity<>(resValue, HttpStatus.OK);
    }

    @PutMapping(value = "/user/approval/{id}")
    public ResponseEntity<Long> approve(@PathVariable Long id) {

        Long resValue = userService.approve(id);

        return new ResponseEntity<>(resValue, HttpStatus.OK);
    }

    @PutMapping(value = "/user/{id}")
    public ResponseEntity<Long> updateUser(@PathVariable Long id,
                                           @RequestBody @Valid UserDto.UserUpdateRequest requestDto,
                                           Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long resValue = userService.update(id, requestDto);

        return new ResponseEntity<>(resValue, HttpStatus.OK);
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<Long> deleteUser(@PathVariable Long id) {

        Long resValue = userService.delete(id);

        return new ResponseEntity<>(resValue, HttpStatus.OK);
    }
}
