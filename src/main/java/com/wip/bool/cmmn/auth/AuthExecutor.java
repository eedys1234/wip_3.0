package com.wip.bool.cmmn.auth;

import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;

import java.util.function.BiFunction;
import java.util.function.Function;

public class AuthExecutor <T, R> {

    public R execute(User user, T t, Function<T, R> adminFunc, BiFunction<Long, T, R> normalFunc) {

        Role role = user.getRole();

        if(role == Role.ROLE_ADMIN) {
            return adminFunc.apply(t);
        }
        else if(role == Role.ROLE_NORMAL) {
            return normalFunc.apply(user.getId(), t);
        }
        else {
            throw new AuthorizationException();
        }
    }
}
