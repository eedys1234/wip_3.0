package com.wip.bool.exception.excp;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NOT_FOUND_APP(400, "A0001", "App 정보가 존재하지 않습니다."),
    NOT_FOUND_BOARD(400, "B0001", "게시물이 존재하지 않습니다."),
    NOT_FOUND_BOOKMARK(400, "U0003", "즐겨찾기가 존재하지 않습니다."),
    NOT_FOUND_CALENDAR(400, "C0001", "일정이 존재하지 않습니다."),
    NOT_FOUND_DEPT(400, "D0001", "부서가 존재하지 않습니다."),
    NOT_FOUND_GROUP(400, "G0001", "그룹이 존재하지 않습니다."),
    NOT_FOUND_GROUP_MEMBER(400, "G0002", "그룹에 속해있지 않습니다."),
    NOT_FOUND_GUITAR_CODE(400, "S0005", "기타코드가 존재하지 않습니다."),
    NOT_FOUND_MP3(400, "S0004", "MP3 파일이 존재하지 않습니다."),
    NOT_FOUND_POSITION(400, "P0001", "직위가 존재하지 않습니다."),
    NOT_FOUND_REPLY(400, "B0002", "댓글이 존재하지 않습니다."),
    NOT_FOUND_SHEET(400, "S0003", "악보가 존재하지 않습니다."),
    NOT_FOUND_SONG(400, "S0002", "곡이 존재하지 않습니다."),
    NOT_FOUND_SONG_MASTER(400, "S0001", "분류가 존재하지 않습니다."),
    NOT_FOUND_USER_BOX(400, "U0004", "사용자 Box 정보가 없습니다."),
    NOT_FOUND_USER(400, "U0001", "사용자가 존재하지 않습니다."),
    NOT_FOUND_RIGHT(400, "R0001", "권한정보가 존재하지 않습니다."),
    NOT_FOUND_WORDS_MASTER(400, "W0001", "단원정보가 존재하지 않습니다"),
    CREATE_FAIL_FILE(400, "F0001", "파일생성에 실패했습니다."),
    DELETE_FAIL_FILE(400, "F0002", "파일삭제에 실패했습니다."),
    AUTHORIZATION(401, "A0002", "권한이 존재하지 않습니다.");

    private int status;
    private String code;
    private String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
