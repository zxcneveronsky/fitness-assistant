package com.example.fitness_assistant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Здоровье сервиса", description = "Мониторинг состояния приложения и базы данных")
public class HealthController {

    private final DataSource dataSource;

    public HealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Operation(
            summary = "Статус сервиса",
            description = """
                    Возвращает текущее состояние приложения и подключения к БД.
                    
                    Возможные статусы:
                    - `UP` — всё работает нормально
                    - `DEGRADED` — приложение работает, но БД недоступна
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Статус получен",
            content = @Content(schema = @Schema(example = """
                    {
                      "status": "UP",
                      "database": "UP",
                      "timestamp": "2026-03-22T20:00:00",
                      "application": "Fitness Assistant",
                      "version": "1.0.0"
                    }
                    """))
    )
    @GetMapping
    public Map<String, Object> getHealth() {
        String dbStatus = getDatabaseStatus();
        return Map.of(
                "status", dbStatus.equals("UP") ? "UP" : "DEGRADED",
                "database", dbStatus,
                "timestamp", LocalDateTime.now().toString(),
                "application", "Fitness Assistant",
                "version", "1.0.0"
        );
    }

    private String getDatabaseStatus() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(1) ? "UP" : "DOWN";
        } catch (Exception e) {
            return "DOWN";
        }
    }
}