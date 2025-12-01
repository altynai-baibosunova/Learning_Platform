package com.altynai.internship.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.altynai.internship.config.ApiKeyProvider;

import java.util.List;
import java.util.Map;

/**
 * GeminiClientService handles direct communication with the Google Gemini API.
 */
@Service
public class GeminiClientService {

    private final RestTemplate restTemplate;
    private final ApiKeyProvider apiKeyProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public GeminiClientService(RestTemplate restTemplate, ApiKeyProvider apiKeyProvider) {
        this.restTemplate = restTemplate;
        this.apiKeyProvider = apiKeyProvider;
    }

    public String getGeminiResponse(String message) {
        try {
            // Retrieve API key and base URL from environment variables
            String apiKey = apiKeyProvider.getGeminiApiKey();
            String baseUrl = apiKeyProvider.getGeminiApiUrl();

            // Correct Gemini API endpoint (v1, not v1beta)
            String apiUrl = baseUrl + "/v1/models/gemini-2.0-flash:generateContent";

            // Request Markdown-formatted response
            String prompt = "Please respond in Markdown format: " + message;

            // Build the request body for Gemini API
            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of(
                                    "parts", List.of(Map.of("text", message))
                            )
                    )
            );

            // Set request headers with JSON content type and API key
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-goog-api-key", apiKey);

            // Combine headers and body into a single HTTP entity
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Send POST request to Gemini API
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Get and validate response body
            String responseBody = response.getBody();
            if (responseBody == null) return "No response from Gemini API.";

            // Parse JSON response
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            JsonNode textNode = jsonNode
                    .path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text");

            // Handle empty response cases
            if (textNode.isMissingNode() || textNode.asText().isEmpty()) {
                return "Gemini returned empty response.";
            }

            // Return the model's text output
            return textNode.asText();

        } catch (Exception e) {
            e.printStackTrace();
            // Catch and return any exception messages
            return "Error contacting Gemini API: " + e.getMessage();
        }
    }
}
