package com.wip.bool;

import com.wip.bool.domain.cmmn.CodeMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CodeMapper mapper() {
        return new CodeMapper();
    }
}
