package com.wip.bool.jwt;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

public class Cookies {

    public String getCookieValue(HttpServletRequest request, String key) {

        if(!Objects.isNull(request.getCookies())) {

            return Arrays.stream(request.getCookies())
                    .filter(cookie -> key.equals(cookie.getName()))
                    .findFirst().orElseThrow(()-> new IllegalStateException()).getValue();

        }

        return null;
    }
}
