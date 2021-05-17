package com.wip.bool.cmmn.right;

import com.wip.bool.cmmn.auth.Authority;
import com.wip.bool.cmmn.auth.Target;
import com.wip.bool.right.domain.Right;

public class RightFactory {

    public static Right getRight() {
        Right right = Right.of(Target.USERBOX, 1L, Authority.GROUP, 1L);
        return right;
    }
}
