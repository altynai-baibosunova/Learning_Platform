package com.altynai.internship.service;

import com.altynai.internship.dto.UserLoginDto;
import com.altynai.internship.dto.UserRegistrationDto;
import com.altynai.internship.model.User;

import java.util.Optional;

public interface UserService {

    User registerUser(UserRegistrationDto userData);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean checkPassword(String rawPassword, String encodedPassword);

    boolean loginUser(UserLoginDto dto);

    User loadUserByUsername(String username);
}