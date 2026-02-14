package com.example.fitness_assistant.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientConfig {

    @Bean
    @Qualifier("spoonRestClient")
    public RestClient spoonacularRestClient(@Value("${api.spoonacular.base-url}") String baseURL){
        return RestClient.builder()
                .baseUrl(baseURL)
                .build();

    }

    @Bean
    @Qualifier("offRestClient")
    public RestClient offRestClient(@Value("${api.off.base-url:https://world.openfoodfacts.org}") String baseURL){
        return RestClient.builder()
                .baseUrl(baseURL)
                .build();
    }

}
