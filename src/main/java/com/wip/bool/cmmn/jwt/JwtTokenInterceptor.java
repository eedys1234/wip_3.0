package com.wip.bool.cmmn.jwt;

import com.wip.bool.cmmn.util.AnnotationUtils;
import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.security.AuthConstants;
import com.wip.bool.security.Permission;
import com.wip.bool.user.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(!(handler instanceof HandlerMethod)) {
            return true;
        }

        final String header = request.getHeader(AuthConstants.AUTH_HEADER);

        if(header != null) {

            final String token = jwtTokenProvider.getTokenFromHeader(header);
            if(jwtTokenProvider.isValidateToken(token) || !jwtTokenProvider.isExpireToken(token)) {

                // token으로부터 추출된 권한에 따른 url 체킹 로직
                Role apiRole = getProperRole((HandlerMethod)handler);
                Role userRole = jwtTokenProvider.getRoleFromToken(token);

                if(apiRole.getPriority() > userRole.getPriority()) {
                    throw new AuthorizationException();
                }

                return true;
            }
        }

        throw new AuthorizationException();
    }

    private Role getProperRole(HandlerMethod handlerMethod) {

        Permission classAnnotation = handlerMethod.getBean().getClass().getAnnotation(Permission.class);
        Permission methodAnnotation = handlerMethod.getBean().getClass().getAnnotation(Permission.class);
        Permission priority = AnnotationUtils.getPermissionPriority(classAnnotation, methodAnnotation);
        return priority.target();
    }
}
