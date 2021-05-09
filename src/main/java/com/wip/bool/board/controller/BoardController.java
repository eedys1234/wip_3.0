package com.wip.bool.board.controller;

import com.wip.bool.board.dto.BoardDto;
import com.wip.bool.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequestMapping(value = "/api/v1")
public class BoardController {

    private final BoardService boardService;

    @PostMapping(value = "/board")
    public ResponseEntity<Long> saveBoard(@Valid @RequestBody BoardDto.BoardSaveRequest requestDto,
                                     @RequestHeader("userId") Long userId,
                                     Errors errors, UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = boardService.saveBoard(userId, requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping(value = "/boards")
    public ResponseEntity<List<BoardDto.BoardSimpleResponse>> findBoards(
                                            @RequestParam("board") String board,
                                            @RequestParam("size") int size,
                                            @RequestParam("offset") int offset) {

        return ResponseEntity.ok(boardService.findBoards(board, size, offset));
    }

    @GetMapping(value = "/board/{boardId:[\\d]+}")
    public ResponseEntity<BoardDto.BoardResponse> findDetailBoard(@PathVariable("boardId") Long boardId) {
        return ResponseEntity.ok(boardService.findDetailBoard(boardId));
    }

    @DeleteMapping(value = "/board/{boardId:[\\d]+}")
    public ResponseEntity<Long> deleteBoard(@PathVariable Long boardId,
                                       @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(boardService.deleteBoard(userId, boardId));
    }

    @PutMapping(value = "/board/{boardId:[\\d]+}/hidden")
    public ResponseEntity<Long> hiddenBoard(@PathVariable Long boardId,
                                            @RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(boardService.hiddenBoard(userId, boardId));
    }
}
