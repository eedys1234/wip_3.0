package com.wip.bool.domain.user;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    REQUEST("REQUEST", "요청"),
    GUEST("GUEST", "손님"),
    NOMARL("NOMARL", "일반 사용자"),
    ADMIN("ADMIN", "관리자"),
    DELETE("DELETE", "삭제된 사용자");

    private final String key;
    private final String value;
}
