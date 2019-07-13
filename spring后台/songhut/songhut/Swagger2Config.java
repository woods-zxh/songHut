package com.songhut.songhut;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger2API文档的配置
 */
@Configuration
public class Swagger2Config {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 被注解的包路径
                .apis(RequestHandlerSelectors.basePackage("com.songhut.songhut.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("songhut后台")
                .description("songhut后台SwaggerUI演示")
                // 文档作者信息
                .contact(new Contact("Kun", "https://github.com/skyparadekun","82349872@qq.com"))
                .version("1.0")
                .build();
    }
}
