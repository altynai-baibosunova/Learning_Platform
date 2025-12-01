package com.altynai.internship.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Provides access to sensitive Gemini API configuration values.
 * Values are loaded from application.yml or environment variables.
 */
@Component
public class ApiKeyProvider {

    @Value("${GEMINI_API_KEY:test-key}")
    private String geminiApiKey;

    @Value("${GEMINI_API_URL:https://fake.url}")
    private String geminiApiUrl;

    public String getGeminiApiKey() {
        return geminiApiKey;
    }

    public String getGeminiApiUrl() {
        return geminiApiUrl;
    }
}
