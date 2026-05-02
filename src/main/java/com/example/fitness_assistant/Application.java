package com.example.fitness_assistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

// https://claude.ai/share/dfd62c7f-089e-43fd-910d-70d92b16ebf4
@EnableCaching
@SpringBootApplication
public class Application {

	public static void main(String[] args) {SpringApplication.run(Application.class, args);}
}

