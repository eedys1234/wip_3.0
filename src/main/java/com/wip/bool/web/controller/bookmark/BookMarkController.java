package com.wip.bool.web.controller.bookmark;

import com.wip.bool.service.bookmark.BookMarkService;
import com.wip.bool.web.dto.bookmark.BookMarkDto;
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

    @PostMapping(value = "/bookmark")
    public ResponseEntity<Long> save(@Valid @RequestBody BookMarkDto.BookMarkSaveReqeust reqeustDto,
                                     @RequestHeader("userId") Long userId,
                                     Errors errors, UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = bookMarkService.save(userId, reqeustDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());
        return new ResponseEntity<>(id, httpHeaders, HttpStatus.OK);
    }

    @DeleteMapping(value = "/bookmark/{bookMarkId:[\\d]+}")
    public ResponseEntity<Long> delete(@PathVariable("bookMarkId") Long bookMarkId) {
        return new ResponseEntity<>(bookMarkService.delete(bookMarkId), HttpStatus.OK);
    }

    @GetMapping(value = "/bookmarks")
    public ResponseEntity<List<BookMarkDto.BookMarkResponse>> gets(
            @RequestHeader("userId") Long userId,
            @RequestParam("sort") String sort,
            @RequestParam("order") String order,
            @RequestParam("size") int size,
            @RequestParam("offset") int offset) {

        return new ResponseEntity<>(bookMarkService.gets(userId, sort, order, size, offset), HttpStatus.OK);
    }
}
