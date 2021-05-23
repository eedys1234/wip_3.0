package com.wip.bool.cmmn.rights;

import com.wip.bool.cmmn.auth.Authority;
import com.wip.bool.cmmn.auth.Target;
import com.wip.bool.rights.domain.Rights;
import org.springframework.test.util.ReflectionTestUtils;

public class RightsFactory {

    public static final String rightType = "read";

    public static Rights getRights() {
        Rights right = Rights.of(Target.USERBOX, 1L, Authority.GROUP, 1L);
        return right;
    }

    public static Rights getRights(Target target, Long targetId, Authority authority, Long authorityId) {
        Rights right = Rights.of(target, targetId, authority, authorityId);
        return right;
    }

    public static Rights getUserBoxRightsWithUser(Long targetId, Long authorityId) {
        Rights rights = Rights.of(Target.USERBOX, targetId, Authority.USER, authorityId);
        return rights;
    }

    public static Rights getUserBoxRightsWithUser(Long targetId, Long authorityId, Long id) {
        Rights rights = Rights.of(Target.USERBOX, targetId, Authority.USER, authorityId);
        ReflectionTestUtils.setField(rights, "id", id);
        return rights;
    }

    public static Rights getUserBoxRightsWithUser(Long targetId, Long authorityId, String rightType, Long id) {
        Rights rights = Rights.of(Target.USERBOX, targetId, Authority.USER, authorityId, rightType);
        ReflectionTestUtils.setField(rights, "id", id);
        return rights;
    }
}
