package com.wip.bool.board.controller;

import com.wip.bool.board.dto.BoardDto;
import com.wip.bool.board.service.BoardService;
import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.security.Permission;
import com.wip.bool.user.domain.Role;
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

    @Permission(target = Role.ROLE_NORMAL)
    @PostMapping(value = "/board")
    public ResponseEntity<ApiResponse<Long>> saveBoard(@Valid @RequestBody BoardDto.BoardSaveRequest requestDto,
                                                      @RequestHeader("userId") Long userId,
                                                      Errors errors, UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = boardService.saveBoard(userId, requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(ApiResponse.of(HttpStatus.CREATED.value(), id), httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/boards")
    public ResponseEntity<ApiResponse<List<BoardDto.BoardSimpleResponse>>> findBoards(
                                            @RequestParam("board") String board,
                                            @RequestParam("size") int size,
                                            @RequestParam("offset") int offset) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), boardService.findBoards(board, size, offset)));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/board/{boardId:[\\d]+}")
    public ResponseEntity<ApiResponse<BoardDto.BoardResponse>> findDetailBoard(@PathVariable("boardId") Long boardId) {
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), boardService.findDetailBoard(boardId)));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @DeleteMapping(value = "/board/{boardId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> deleteBoard(@PathVariable Long boardId,
                                       @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), boardService.deleteBoard(userId, boardId)));
    }

    @Permission(target = Role.ROLE_ADMIN)
    @PutMapping(value = "/board/{boardId:[\\d]+}/hidden")
    public ResponseEntity<ApiResponse<Long>> hiddenBoard(@PathVariable Long boardId,
                                            @RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), boardService.hiddenBoard(userId, boardId)));
    }
}
