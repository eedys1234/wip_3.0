package com.wip.bool.configure;

import com.wip.bool.cmmn.auth.AuthInterceptor;
import com.wip.bool.cmmn.jwt.JwtTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
        .excludePathPatterns("/api/v1/user", "/api/v1/user/wip-login", "/api/v1/user/login");
        registry.addInterceptor(authInterceptor())
        .excludePathPatterns("/api/v1/user", "/api/v1/user/wip-login", "/api/v1/user/login");
    }

    @Bean
    public JwtTokenInterceptor jwtInterceptor() {
        return new JwtTokenInterceptor();
    }

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }
}
