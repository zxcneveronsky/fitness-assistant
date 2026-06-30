package com.example.fitness_assistant.application.service.user;

import com.example.fitness_assistant.core.exception.InvalidPasswordException;
import com.example.fitness_assistant.core.model.User;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.core.security.PasswordEncoder;
import com.example.fitness_assistant.core.security.TokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class LoginUserUseCaseTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    TokenProvider tokenProvider;
    @InjectMocks
    LoginUserUseCase loginUserUseCase;

    @Test
    public void loginUserTest(){
        User testUser = new User(1L,"s@m","123", User.Role.USER);

        when(userRepository.findByEmail("s@m")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("123","123")).thenReturn(Boolean.TRUE);
        when(tokenProvider.generateToken(testUser)).thenReturn("ggz");

        LoginResult testLoginResult = loginUserUseCase.loginUser("s@m","123");

        assertEquals("ggz",testLoginResult.token());
        assertThrows(InvalidPasswordException.class,()->loginUserUseCase.loginUser("s@m","1231"));

    }
}
