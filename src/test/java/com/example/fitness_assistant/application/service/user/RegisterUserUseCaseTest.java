package com.example.fitness_assistant.application.service.user;

import com.example.fitness_assistant.core.exception.UserAlreadyExistsException;
import com.example.fitness_assistant.core.model.User;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.core.security.PasswordEncoder;
import com.example.fitness_assistant.core.security.TokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegisterUserUseCaseTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    TokenProvider tokenProvider;
    @InjectMocks
    RegisterUserUseCase registerUserUseCase;

    @Test
    public void registerUserTest(){
        User testUser = new User(1L,"s@m","123", User.Role.USER);
        User testUser0 = new User(1L,"ss@m","123", User.Role.USER);

        when(userRepository.existsByEmail("s@m")).thenReturn(Boolean.FALSE);
        when(userRepository.existsByEmail("ss@m")).thenReturn(Boolean.TRUE);
        when(passwordEncoder.encode("123")).thenReturn("123");
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(tokenProvider.generateToken(testUser)).thenReturn("ggz");

        LoginResult testLoginResult = registerUserUseCase.registerUser(testUser);

        assertEquals("ggz",testLoginResult.token());
        assertThrows(UserAlreadyExistsException.class,() -> registerUserUseCase.registerUser(testUser0));

    }
}
