package com.wip.bool.bookmark.controller;

import com.wip.bool.bookmark.service.BookMarkService;
import com.wip.bool.bookmark.dto.BookMarkDto;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class BookMarkController {

    private final BookMarkService bookMarkService;

    @Permission(target = Role.ROLE_NORMAL)
    @PostMapping(value = "/bookmark")
    public ResponseEntity<ApiResponse<Long>> saveBookMark(@Valid @RequestBody BookMarkDto.BookMarkSaveRequest requestDto,
                                                          @RequestHeader("userId") Long userId,
                                                          Errors errors,
                                                          UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = bookMarkService.saveBookMark(userId, requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());
        return new ResponseEntity<>(ApiResponse.of(HttpStatus.CREATED.value(), id), httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_NORMAL)
    @DeleteMapping(value = "/bookmark/{bookMarkId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> deleteBookMark(@PathVariable Long bookMarkId,
                                                            @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),bookMarkService.deleteBookMark(userId, bookMarkId)));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/bookmarks")
    public ResponseEntity<ApiResponse<List<BookMarkDto.BookMarkResponse>>> findBookMarks(@RequestHeader("userId") Long userId,
                                                                            @RequestParam String sort,
                                                                            @RequestParam String order,
                                                                            @RequestParam int size,
                                                                            @RequestParam int offset) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), bookMarkService.findBookMarks(userId, sort, order, size, offset)));
    }
}
