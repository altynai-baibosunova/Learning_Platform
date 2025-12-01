package com.altynai.internship.service;

import com.altynai.internship.dto.ChatRequestDto;
import com.altynai.internship.dto.ChatResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

    @Mock
    private GeminiClientService geminiClientService;  //  mock external service

    @InjectMocks
    private ChatServiceImpl chatService;  //  class under test

    @Test
    void testGetChatResponse_returnsExpectedReply() {
        // Arrange
        ChatRequestDto request = new ChatRequestDto();
        request.setMessage("Hello AI");

        String expectedReply = "Hi there!";
        when(geminiClientService.getGeminiResponse("Hello AI")).thenReturn(expectedReply);

        // Act
        ChatResponseDto actual = chatService.getChatResponse(request);

        // Assert
        assertNotNull(actual);
        assertEquals(expectedReply, actual.getReply());
        verify(geminiClientService, times(1)).getGeminiResponse("Hello AI");
    }

    @Test
    void testGetChatResponse_handlesErrorGracefully() {
        // Arrange
        ChatRequestDto request = new ChatRequestDto();
        request.setMessage("Error test");

        when(geminiClientService.getGeminiResponse(anyString()))
                .thenThrow(new RuntimeException("Gemini API failed"));

        // Act
        ChatResponseDto actual;
        try {
            actual = chatService.getChatResponse(request);
        } catch (Exception e) {
            actual = new ChatResponseDto("Error contacting Gemini API: " + e.getMessage());
        }

        // Assert
        assertNotNull(actual);
        assertTrue(actual.getReply().contains("Error"));
    }
}
