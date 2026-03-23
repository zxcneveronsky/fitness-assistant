package com.example.fitness_assistant.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fitness Assistant API")
                        .description("""
                                REST API фитнес-ассистента.
                                
                                Позволяет искать упражнения по группам мышц, получать КБЖУ продуктов,
                                управлять профилем пользователя и отслеживать прогресс.
                                
                                Для защищённых эндпоинтов необходимо передавать JWT-токен.
                                Получить токен можно через /api/auth/register или /api/auth/login.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Стафеев Григорий, Синянский Вениамин, Онучин Тимофей")
                                .email("fitness-assistant@130.ru")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Вставь JWT токен полученный при входе/регистрации")));
    }
}