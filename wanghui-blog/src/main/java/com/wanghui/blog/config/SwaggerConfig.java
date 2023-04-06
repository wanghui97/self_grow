package com.wanghui.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wanghui.blog.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("个人", "http://www.my.com", "my@my.com");
        return new ApiInfoBuilder()
                .title("个人博前台系统接口文档")
                .description("个人博前台系统接口文档")
                .contact(contact)   // 联系方式
                .version("1.1.0")  // 版本
                .build();
    }
}