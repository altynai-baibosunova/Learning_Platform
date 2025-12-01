package com.altynai.internship.service;

import com.altynai.internship.dto.UserLoginDto;
import com.altynai.internship.dto.UserRegistrationDto;
import com.altynai.internship.model.User;
import com.altynai.internship.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User("gulnara", "hashedPassword123", "g@example.com");
        testUser.setId(UUID.randomUUID());
    }

    //   Register new user successfully
    @Test
    void registerUser_createsNewUser_whenEmailAndUsernameUnique() {
        UserRegistrationDto dto = new UserRegistrationDto("gulnara", "123456", "g@example.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(dto.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("hashedPassword123");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userService.registerUser(dto);

        assertNotNull(savedUser);
        assertEquals("gulnara", savedUser.getUsername());
        assertEquals("g@example.com", savedUser.getEmail());

        verify(userRepository).save(any(User.class));
    }

    //   Throw error when email already exists
    @Test
    void registerUser_throwsException_whenEmailExists() {
        UserRegistrationDto dto = new UserRegistrationDto("gulnara", "123456", "g@example.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(testUser));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(dto));

        assertEquals("Email already exists", exception.getMessage());
    }

    //   Throw error when username already exists
    @Test
    void registerUser_throwsException_whenUsernameExists() {
        UserRegistrationDto dto = new UserRegistrationDto("gulnara", "123456", "g@example.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(dto.getUsername())).thenReturn(Optional.of(testUser));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(dto));

        assertEquals("Username already exists", exception.getMessage());
    }

    //   Successful login when password matches
    @Test
    void loginUser_returnsTrue_whenCredentialsAreValid() {
        UserLoginDto dto = new UserLoginDto("g@example.com", "123456");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(dto.getPassword(), testUser.getPasswordHash())).thenReturn(true);

        boolean result = userService.loginUser(dto);

        assertTrue(result);
    }

    //   Login fails when password does not match
    @Test
    void loginUser_returnsFalse_whenPasswordDoesNotMatch() {
        UserLoginDto dto = new UserLoginDto("g@example.com", "wrongpass");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(dto.getPassword(), testUser.getPasswordHash())).thenReturn(false);

        boolean result = userService.loginUser(dto);

        assertFalse(result);
    }

    //   Login fails when email not found
    @Test
    void loginUser_returnsFalse_whenEmailNotFound() {
        UserLoginDto dto = new UserLoginDto("notfound@example.com", "123456");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        boolean result = userService.loginUser(dto);

        assertFalse(result);
    }
}