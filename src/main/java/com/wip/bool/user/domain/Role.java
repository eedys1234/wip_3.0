package com.wip.bool.user.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_NONE("ROLE_NONE", "NONE", 0),
    ROLE_REQUEST("ROLE_REQUEST", "요청", 1),
    ROLE_GUEST("ROLE_GUEST", "손님", 2),
    ROLE_NORMAL("ROLE_NORMAL", "일반 사용자", 3),
    ROLE_ADMIN("ROLE_ADMIN", "관리자", 4),
    ROLE_DELETE("ROLE_DELETE", "삭제된 사용자", -1);

    private final String key;
    private final String value;
    private final int priority;
}
