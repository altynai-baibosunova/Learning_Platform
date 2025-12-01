package com.altynai.internship.service;

import com.altynai.internship.dto.UserLoginDto;
import com.altynai.internship.dto.UserRegistrationDto;
import com.altynai.internship.model.User;
import com.altynai.internship.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    // REGISTER user
    @Override
    public User registerUser(UserRegistrationDto userData) {

        // Step 1: check if email already exists
        Optional<User> existingEmail = userRepository.findByEmail(userData.getEmail());
        if (existingEmail.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Step 2: Check if username already exists
        Optional<User> existingUsername = userRepository.findByUsername(userData.getUsername());
        if (existingUsername.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Step 3: Encode password before saving
        String hashedPassword = passwordEncoder.encode(userData.getPassword());

        // Step 4: Create new user
        User newUser = new User(userData.getUsername(), hashedPassword, userData.getEmail());

        // Step 5: Save to DB
        return userRepository.save(newUser);
    }

    // Find by email
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Find by username
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Login user
    @Override
    public boolean loginUser(UserLoginDto dto) {
        Optional<User> userOptional = userRepository.findByEmail(dto.getEmail());
        if (userOptional.isEmpty()) {
            return false; //email not found
        }

        User user = userOptional.get();
        return passwordEncoder.matches(dto.getPassword(), user.getPasswordHash());
    }

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Password check (used by controller)
    @Override
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}