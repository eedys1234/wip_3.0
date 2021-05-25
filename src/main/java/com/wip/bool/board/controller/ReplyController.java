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

    @PostMapping(value = "/board/{boardId:[\\d]+}/reply")
    public ResponseEntity<Long> saveReply(@Valid @RequestBody ReplyDto.ReplySaveRequest requestDto,
                                     @PathVariable Long boardId,
                                     @RequestHeader("userId") Long userId,
                                     Errors errors, UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = replyService.saveReply(userId, boardId, requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping(value = "/board/{boardId:[\\d]+}/reply")
    public ResponseEntity<List<ReplyDto.ReplyResponse>> getsByBoard(
                                    @PathVariable Long boardId,
                                    @RequestParam int size,
                                    @RequestParam int offset) {

        return ResponseEntity.ok(replyService.getsByBoard(boardId, size, offset));
    }

    @GetMapping(value = "/board/{boardId:[\\d]+}/reply/{replyId:[\\d]+}")
    public ResponseEntity<List<ReplyDto.ReplyResponse>> getsByReply(@PathVariable Long boardId,
                                                                    @PathVariable Long replyId,
                                                                    @RequestParam int size,
                                                                    @RequestParam int offset) {

        return ResponseEntity.ok(replyService.getsByReply(replyId, size, offset));
    }

    @PutMapping(value = "/board/{boardId:[\\d]+}/reply/{replyId:[\\d]+}")
    public ResponseEntity<Long> updateReply(@PathVariable Long replyId,
                                            @PathVariable Long boardId,
                                            @RequestHeader("userId") Long userId,
                                            @Valid @RequestBody ReplyDto.ReplyUpdateRequest requestDto) {

        return ResponseEntity.ok(replyService.updateReply(userId, replyId, requestDto));
    }

    @DeleteMapping(value = "/board/{boardId:[\\d]+}/reply/{replyId:[\\d]+}")
    public ResponseEntity<Long> deleteReply(@PathVariable Long replyId,
                                       @PathVariable Long boardId,
                                       @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(replyService.deleteReply(userId, replyId));
    }

}
