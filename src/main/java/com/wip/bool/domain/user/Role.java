package com.wip.bool.domain.user;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_REQUEST("ROLE_REQUEST", "요청"),
    ROLE_GUEST("ROLE_GUEST", "손님"),
    ROLE_NORMAL("ROLE_NORMAL", "일반 사용자"),
    ROLE_ADMIN("ROLE_ADMIN", "관리자"),
    ROLE_DELETE("ROLE_DELETE", "삭제된 사용자");

    private final String key;
    private final String value;
}
