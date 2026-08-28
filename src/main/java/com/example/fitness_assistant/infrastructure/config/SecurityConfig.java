package com.example.fitness_assistant.infrastructure.config;

import com.example.fitness_assistant.infrastructure.security.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Value("${app.swagger.enabled:false}")
    private boolean swaggerEnabled;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var permitAllEndpoints = new ArrayList<String>();
        permitAllEndpoints.add("/");
        permitAllEndpoints.add("/index.html");
        permitAllEndpoints.add("/login.html");
        permitAllEndpoints.add("/register.html");
        permitAllEndpoints.add("/dashboard.html");
        permitAllEndpoints.add("/meal.html");
        permitAllEndpoints.add("/hydration.html");
        permitAllEndpoints.add("/workout.html");
        permitAllEndpoints.add("/workout-detail.html");
        permitAllEndpoints.add("/session.html");
        permitAllEndpoints.add("/session-detail.html");
        permitAllEndpoints.add("/history.html");
        permitAllEndpoints.add("/profile.html");
        permitAllEndpoints.add("/exercise-history.html");
        permitAllEndpoints.add("/js/**");
        permitAllEndpoints.add("/explore/**");
        permitAllEndpoints.add("/api/v1/auth/login");
        permitAllEndpoints.add("/api/v1/auth/register");
        permitAllEndpoints.add("/api/v1/food/search");
        permitAllEndpoints.add("/api/v1/food/{id}");
        permitAllEndpoints.add("/api/v1/food/*/calculate");
        permitAllEndpoints.add("/api/v1/exercise/search");
        permitAllEndpoints.add("/api/v1/exercise/{id}");
        permitAllEndpoints.add("/api/v1/muscle/search");
        permitAllEndpoints.add("/api/v1/muscle/{id}");
        if (swaggerEnabled) {
            permitAllEndpoints.add("/swagger-ui.html");
            permitAllEndpoints.add("/swagger-ui/**");
            permitAllEndpoints.add("/v3/api-docs/**");
            permitAllEndpoints.add("/v3/api-docs.yaml");
        }

        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"timestamp\":\"" + java.time.LocalDateTime.now() + "\",\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Требуется авторизация\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write("{\"timestamp\":\"" + java.time.LocalDateTime.now() + "\",\"status\":403,\"error\":\"Forbidden\",\"message\":\"Недостаточно прав\"}");
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(permitAllEndpoints.toArray(new String[0])).permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}