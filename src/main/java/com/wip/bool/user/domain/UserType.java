package com.wip.bool.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {

    GOOGLE("GOOGLE", "구글"),
    NAVER("NAVER", "네이버"),
    KAKAO("KAKAO", "카카오"),
    GITHUB("GITHUB", "깃허브"),
    WIP("WIP", "WIP");

    private final String key;
    private final String value;

}
