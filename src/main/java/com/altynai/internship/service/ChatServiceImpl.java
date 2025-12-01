package com.altynai.internship.service;

import com.altynai.internship.dto.ChatRequestDto;
import com.altynai.internship.dto.ChatResponseDto;
import com.altynai.internship.model.ChatMessage;
import com.altynai.internship.model.ChatSession;
import com.altynai.internship.model.SenderRole;
import com.altynai.internship.repository.ChatMessageRepository;
import com.altynai.internship.repository.ChatSessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * ChatServiceImpl handles chat logic between the controller
 * and the external AI client (GeminiClientService).
 */
@Service
public class ChatServiceImpl implements ChatService {

    private final GeminiClientService geminiClientService;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatServiceImpl(GeminiClientService geminiClientService,
                           ChatSessionRepository chatSessionRepository,
                           ChatMessageRepository chatMessageRepository) {
        this.geminiClientService = geminiClientService;
        this.chatSessionRepository = chatSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public ChatResponseDto getChatResponse(ChatRequestDto request) {
        try{
            // Validate input
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                return new ChatResponseDto("Message cannot be empty.");
            }
            // Load or create chat session
            ChatSession session = getOrCreateSession(request.getSessionId());

            // Save user's message
            ChatMessage userMessage = ChatMessage.builder()
                    .content(request.getMessage())
                    .timestamp(LocalDateTime.now())
                    .sender(SenderRole.USER)
                    .session(session)
                    .build();
            chatMessageRepository.save(userMessage);

            // Get AI response from Gemini
            String aiReply = geminiClientService.getGeminiResponse(request.getMessage());

            // If Gemini returned null or empty, handle gracefully
            if (aiReply == null || aiReply.trim().isEmpty()) {
                aiReply = "No response received from AI.";
            }
            // Save AI's message
            ChatMessage aiMessage = ChatMessage.builder()
                    .content(aiReply)
                    .timestamp(LocalDateTime.now())
                    .sender(SenderRole.AI)
                    .session(session)
                    .build();
            chatMessageRepository.save(aiMessage);

            // Return response DTO
            return new ChatResponseDto(aiReply, session.getId());
        } catch (Exception e) {
            return new ChatResponseDto("Error: " + e.getMessage());
        }
    }

    // Helper: Load existing session or create a new one
    private ChatSession getOrCreateSession(Long sessionId) {
        if(sessionId != null) {
            Optional<ChatSession> existing = chatSessionRepository.findById(sessionId);
            if(existing.isPresent()) {
                return existing.get();
            }
        }
        // Create new session
        ChatSession session = ChatSession.builder()
                .title("New Chat")
                .createdAt(LocalDateTime.now())
                .build();
        return chatSessionRepository.save(session);
    }
}
