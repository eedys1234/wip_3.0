package com.wip.bool.cmmn.util;

import com.wip.bool.user.domain.Role;

public final class RoleUtils {

    public static boolean isRoleAdmin(Role role) {
        return role == Role.ROLE_ADMIN;
    }
}
