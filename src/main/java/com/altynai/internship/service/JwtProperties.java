package com.altynai.internship.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * Secret key used to sign JWT tokens.
     * Can be plain text or Base64 encoded.
     */
    private String secret = "default-secret-key"; // default for dev

    /** Token expiration in milliseconds */
    private long expirationMs = 24 * 60 * 60 * 1000; // default 24 hours

    // Getters & Setters
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public void setExpirationMs(long expirationMs) {
        this.expirationMs = expirationMs;
    }
}
