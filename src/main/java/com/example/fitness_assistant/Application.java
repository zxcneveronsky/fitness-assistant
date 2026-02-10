package com.example.fitness_assistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

///                       http://localhost:8080
@SpringBootApplication
@RestController
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


    @GetMapping("/")
    public String sayHello(){
        return "Здравствуйте!";
    }


    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

}

