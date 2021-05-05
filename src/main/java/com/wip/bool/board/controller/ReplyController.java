package com.wip.bool.board.controller;

import com.wip.bool.board.dto.ReplyDto;
import com.wip.bool.board.service.ReplyService;
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
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping(value = "/board/{board:[\\d]+}/reply")
    public ResponseEntity<Long> save(@Valid @RequestBody ReplyDto.ReplySaveRequest requestDto,
                                     @PathVariable("boardId") Long boardId,
                                     @RequestHeader("userId") Long userId,
                                     Errors errors, UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = replyService.save(userId, boardId, requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping(value = "/board/{boardId:[\\d]+}/reply")
    public ResponseEntity<List<ReplyDto.ReplyResponse>> getsByBoard(
                                    @PathVariable("boardId") Long boardId,
                                    @RequestParam("size") int size,
                                    @RequestParam("offset") int offset) {

        return ResponseEntity.ok(replyService.getsByBoard(boardId, size, offset));
    }

    @GetMapping(value = "/board/{boardId:[\\d]+}/reply/{replyId:[\\d]+}")
    public ResponseEntity<List<ReplyDto.ReplyResponse>> getsByReply(
                                    @PathVariable("boardId") Long boardId,
                                    @PathVariable("replyId") Long replyId,
                                    @RequestParam("size") int size,
                                    @RequestParam("offset") int offset
                                    ) {

        return ResponseEntity.ok(replyService.getsByReply(replyId, size, offset));
    }

    @DeleteMapping(value = "/board/{boardId:[\\d]+}/reply/{replyId:[\\d]+}")
    public ResponseEntity<Long> delete(@PathVariable("replyId") Long replyId,
                                       @PathVariable("boardId") Long boardId,
                                       @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(replyService.delete(userId, replyId));
    }

}
