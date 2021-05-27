package com.wip.bool.board.controller;

import com.wip.bool.board.dto.ReplyDto;
import com.wip.bool.board.service.ReplyService;
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
public class ReplyController {

    private final ReplyService replyService;

    @Permission(target = Role.ROLE_NORMAL)
    @PostMapping(value = "/board/{boardId:[\\d]+}/reply")
    public ResponseEntity<ApiResponse<Long>> saveReply(@Valid @RequestBody ReplyDto.ReplySaveRequest requestDto,
                                                      @PathVariable Long boardId,
                                                      @RequestHeader("userId") Long userId,
                                                      Errors errors, UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = replyService.saveReply(userId, boardId, requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(ApiResponse.of(HttpStatus.CREATED.value(), id), httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/board/{boardId:[\\d]+}/reply")
    public ResponseEntity<ApiResponse<List<ReplyDto.ReplyResponse>>> getsByBoard(
                                    @PathVariable Long boardId,
                                    @RequestParam int size,
                                    @RequestParam int offset) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), replyService.getsByBoard(boardId, size, offset)));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/board/{boardId:[\\d]+}/reply/{replyId:[\\d]+}")
    public ResponseEntity<ApiResponse<List<ReplyDto.ReplyResponse>>> getsByReply(@PathVariable Long boardId,
                                                                    @PathVariable Long replyId,
                                                                    @RequestParam int size,
                                                                    @RequestParam int offset) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), replyService.getsByReply(replyId, size, offset)));
    }

    @PutMapping(value = "/board/{boardId:[\\d]+}/reply/{replyId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> updateReply(@PathVariable Long replyId,
                                            @PathVariable Long boardId,
                                            @RequestHeader("userId") Long userId,
                                            @Valid @RequestBody ReplyDto.ReplyUpdateRequest requestDto) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), replyService.updateReply(userId, replyId, requestDto)));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @DeleteMapping(value = "/board/{boardId:[\\d]+}/reply/{replyId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> deleteReply(@PathVariable("replyId") Long replyId,
                                       @PathVariable("boardId") Long boardId,
                                       @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), replyService.deleteReply(userId, replyId)));
    }

}
