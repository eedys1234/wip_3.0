package com.wip.bool.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.user.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private ObjectMapper objectMapper;

    public CustomAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        final UsernamePasswordAuthenticationToken authRequest;
        final UserDto.UserLoginRequest user;

        try {
            //비밀번호 복호화 해야함(bouncy castle 라이브러리 사용!!)
            user = objectMapper.readValue(request.getInputStream(), UserDto.UserLoginRequest.class);
            authRequest = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getUserPassword());

        } catch (IOException e) {
            throw new IllegalArgumentException("로그인에 실패하였습니다. ");
        }

        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
