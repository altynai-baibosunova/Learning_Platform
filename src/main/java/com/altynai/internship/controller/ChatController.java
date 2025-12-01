package com.altynai.internship.controller;

import com.altynai.internship.dto.ChatRequestDto;
import com.altynai.internship.dto.ChatResponseDto;
import com.altynai.internship.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ChatController handles chat messages between authenticated users
 * and the external AI service (e.g., Gemini API).
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    // Constructor Injection (recommended for immutability and testing)
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Endpoint: POST /api/chat/message
     * Accepts a user's message and returns AI response.
     */
    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestBody ChatRequestDto request) {
        try {
            ChatResponseDto response = chatService.getChatResponse(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Catch all exceptions and return a clean error message to frontend
            return ResponseEntity
                    .badRequest()
                    .body("Error processing message: " + e.getMessage());
        }
    }
}
