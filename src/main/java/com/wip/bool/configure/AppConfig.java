package com.wip.bool.configure;

import com.wip.bool.cmmn.CodeMapper;
import com.wip.bool.cmmn.CodeModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CodeMapper<CodeModel> mapper() {
        return new CodeMapper<>();
    }
}
