package com.wip.bool.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Profile({"local", "dev"})
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private final String VERSION = "v1";
    private final String TITLE = "WIP API ";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(VERSION)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo(TITLE, VERSION));
    }

    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfo(
            title,
            "Swagger create Api docs",
            version,
            "www.bool.wip.com",
            new Contact("Contact Me", "www.bool.wip.com", "eedys123@gmail.com"),
            "License",
            "www.bool.wip.com",
            new ArrayList<>()
        );
    }
}
