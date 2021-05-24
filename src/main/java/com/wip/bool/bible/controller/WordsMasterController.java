package com.wip.bool.bible.controller;

import com.wip.bool.bible.dto.WordsMasterDto;
import com.wip.bool.bible.service.WordsMasterService;
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
public class WordsMasterController {

    private final WordsMasterService wordsMasterService;

    @Permission(target = Role.ROLE_ADMIN)
    @PostMapping(value = "/words-master")
    public ResponseEntity<Long> saveWordsMaster(@Valid @RequestBody WordsMasterDto.WordsMasterSaveRequest requestDto,
                                                @RequestHeader("userId") Long userId,
                                                Errors errors,
                                                UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = wordsMasterService.saveWordsMaster(userId, requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_ADMIN)
    @DeleteMapping(value = "/words-master/{wordsMasterId:[\\d]+}")
    public ResponseEntity<Long> deleteWordsMaster(@PathVariable Long wordsMasterId,
                                                  @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(wordsMasterService.deleteWordsMaster(userId, wordsMasterId));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/words-masters")
    public ResponseEntity<List<WordsMasterDto.WordsMasterResponse>> findAll() {
        return ResponseEntity.ok(wordsMasterService.findAll());
    }
}


