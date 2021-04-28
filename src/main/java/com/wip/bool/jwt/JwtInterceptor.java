package com.wip.bool.jwt;

import com.wip.bool.domain.user.Role;
import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.security.AuthConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final String header = request.getHeader(AuthConstants.AUTH_HEADER);

        if(header != null) {
            final String token = jwtTokenProvider.getTokenFromHeader(header);
            if(jwtTokenProvider.isValidateToken(token) || !jwtTokenProvider.isExpireToken(token)) {

                // token으로부터 추출된 권한에 따른 url 체킹 로직
                Role role = jwtTokenProvider.getRoleFromToken(token);


                return true;
            }
        }

        throw new AuthorizationException();
    }
}
