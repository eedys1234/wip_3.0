package com.wip.bool.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        final UsernamePasswordAuthenticationToken authRequest;

        try {
            final UserDto.UserLoginRequest user = new ObjectMapper().readValue(request.getInputStream(), UserDto.UserLoginRequest.class);
            authRequest = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getUserPassword());

        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
        setDetails(request, authRequest);
        return authenticationManager.authenticate(authRequest);
    }
}
