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


//1. Глобальные бренды (самые надежные для теста)
//Эти продукты есть в базе во всех странах, у них всегда заполнены КБЖУ:
//
//        3017620422003 — Nutella (600г). Тут будет много жиров и сахара.
//
//        5449000000996 — Coca-Cola (классическая, 0.5л). Проверь, как отобразятся углеводы.
//
//        7622210449283 — Печенье Oreo.
//
//        5000159407236 — Батончик Snickers.
//
//        2. Продукты, которые часто встречаются в РФ
//        Популярные товары, которые должны выдать названия на русском или английском:
//
//        4607001771144 — Кириешки (ветчина с сыром).
//
//        4606272021135 — Напиток Добрый (Апельсин).
//
//        4600680004526 — Сгущенное молоко (Алексеевское).
//
//        4600494635198 — Майонез Слобода.
//
//        3. "Здоровая" еда (для фитнес-теста)

//        3038350008500 — Консервированный тунец (много белков!).
//
//        4002632201306 — Хлебцы (цельнозерновые).
//
//        5900649062994 — Безлактозное молоко.
