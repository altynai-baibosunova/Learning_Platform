package com.altynai.internship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.altynai.internship.dto.ChatRequestDto;
import com.altynai.internship.dto.ChatResponseDto;
import com.altynai.internship.service.ChatService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatController.class)
@AutoConfigureMockMvc(addFilters = false)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChatService chatService;

    @MockBean
    private com.altynai.internship.config.JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void sendMessage_returnsAIResponse() throws Exception {
        ChatRequestDto requestDto = new ChatRequestDto();
        requestDto.setMessage("Hello AI");

        ChatResponseDto responseDto = new ChatResponseDto();
        responseDto.setReply("Hi there!");

        Mockito.when(chatService.getChatResponse(any(ChatRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/chat/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }
}
