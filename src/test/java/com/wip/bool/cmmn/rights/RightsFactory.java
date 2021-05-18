package com.wip.bool.cmmn.rights;

import com.wip.bool.cmmn.auth.Authority;
import com.wip.bool.cmmn.auth.Target;
import com.wip.bool.rights.domain.Rights;

public class RightsFactory {

    public static Rights getRights() {
        Rights right = Rights.of(Target.USERBOX, 1L, Authority.GROUP, 1L);
        return right;
    }

    public static Rights getRights(Target target, Long targetId, Authority authority, Long authorityId) {
        Rights right = Rights.of(target, targetId, authority, authorityId);
        return right;
    }
}
