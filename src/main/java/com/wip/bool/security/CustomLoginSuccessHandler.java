package com.wip.bool.security;

import com.wip.bool.domain.user.CustomUser;
import com.wip.bool.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) {

        final String token = jwtTokenProvider.createToken(((CustomUser)authentication.getPrincipal()).getUser());
        response.addHeader(AuthConstants.AUTH_HEADER, String.format("%s %s",AuthConstants.TOKEN_TYPE, token));
    }
}
