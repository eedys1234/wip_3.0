package com.wip.bool.exception;

import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.exception.excp.not_found.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(NotFoundFileException.class)
    public ResponseEntity<Void> handleNotFoundFileException(NotFoundFileException e) {
        logging(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(NotFoundAppVersionException.class)
    public ResponseEntity<Void> handleNotFoundAppVersionException(NotFoundAppVersionException e) {
        logging(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(NotFoundBoardException.class)
    public ResponseEntity<Void> handleNotFoundBoardException(NotFoundBoardException e) {
        logging(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(NotFoundReplyException.class)
    public ResponseEntity<Void> handleNotFoundReplyException(NotFoundReplyException e) {
        logging(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(NotFoundBookMarkException.class)
    public ResponseEntity<Void> handleNotFoundBookMarkException(NotFoundBookMarkException e) {
        logging(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(NotFoundCalendarException.class)
    public ResponseEntity<Void> handleNotFoundCalendarException(NotFoundCalendarException e) {
        logging(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(NotFoundGroupException.class)
    public ResponseEntity<Void> handleNotFoundGroupException(NotFoundGroupException e) {
        logging(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(NotFoundGroupMemberException.class)
    public ResponseEntity<Void> handleNotFoundGroupMemberException(NotFoundGroupMemberException e) {
        logging(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(NotFoundGuitarCodeException.class)
    public ResponseEntity<Void> handleNotFoundGuitarCodeException(NotFoundGuitarCodeException e) {
        logging(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(NotFoundMP3Exception.class)
    public ResponseEntity<Void> handleNotFoundMP3Exception(NotFoundMP3Exception e) {
        logging(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(NotFoundSheetException.class)
    public ResponseEntity<Void> handleNotFoundSheetException(NotFoundSheetException e) {
        logging(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(NotFoundSongException.class)
    public ResponseEntity<Void> handleNotFoundSongException(NotFoundSongException e) {
        logging(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(NotFoundSongMasterException.class)
    public ResponseEntity<Void> handleNotFoundSongMasterException(NotFoundSongMasterException e) {
        logging(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(NotFoundPositionException.class)
    public ResponseEntity<Void> handleNotFoundPositionException(NotFoundPositionException e) {
        logging(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(NotFoundDeptException.class)
    public ResponseEntity<Void> handleNotFoundDeptException(NotFoundDeptException e) {
        logging(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(NotFoundUserBoxException.class)
    public ResponseEntity<Void> handleNotFoundUserBoxException(NotFoundUserBoxException e) {
        logging(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Void> handleIllegalStateException(IllegalStateException e) {
        logging(e);
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        logging(e);
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Void> handleAuthorizationException(AuthorizationException e) {
        logging(e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private void logging(Exception e) {
        log.error(e.getMessage());
    }
}
