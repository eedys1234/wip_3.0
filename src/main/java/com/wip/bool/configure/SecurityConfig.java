package com.wip.bool.configure;

import com.wip.bool.domain.user.Role;
import com.wip.bool.exception.auth.CustomAuthenticationEntryPoint;
import com.wip.bool.jwt.JwtAuthenticationFilter;
import com.wip.bool.jwt.JwtTokenProvider;
import com.wip.bool.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic().disable()
        .csrf().disable()
//        .headers().frameOptions().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
        .antMatchers("/api/v1/user").permitAll()
        .antMatchers("/api/v1/**").hasRole(Role.NOMARL.getKey())
        .antMatchers("/api/v1/user/approval", "/api/v1/user/{id}").hasRole(Role.ADMIN.getKey())
        .anyRequest().authenticated()
        .and()
        .oauth2Login().userInfoEndpoint().userService(userService).and()
        .and()
        .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        .and()
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
        ;
    }

    /**
     * swagger를 사용할 경우 제외
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
}
