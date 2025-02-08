package com.luckyparty.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .components(new Components())
                .info(info());
    }

    public Info info(){
        return new Info()
                .title("운빨파티 Swagger")
                .version("1.0")
                .description("운빨파티 프로젝트 백엔드 API Swagger");
    }
}
