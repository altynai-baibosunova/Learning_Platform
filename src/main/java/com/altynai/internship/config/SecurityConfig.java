package com.altynai.internship.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures application security: public routes, JWT filter, and stateless session policy.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF because we are using stateless JWT authentication (no cookies)
                .csrf(csrf -> csrf.disable())

                // Enable CORS (configured globally in WebConfig)
                .cors(cors -> {})

                // Use stateless session management for JWT
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Define which endpoints are public and which require authentication
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()   // Public: login, register
                        .requestMatchers("/api/chat/**").authenticated() // Protected: chat endpoints
                        .anyRequest().authenticated()
                )

                // Disable default login form (not needed for APIs)
                .formLogin(form -> form.disable())

                // Disable basic authentication (we only use JWT)
                .httpBasic(basic -> basic.disable())

                // Disable frame options (safe since H2 console is removed, CI requires this)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // Add our custom JWT filter before Spring's authentication filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Expose AuthenticationManager as a bean (required for AuthController)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
