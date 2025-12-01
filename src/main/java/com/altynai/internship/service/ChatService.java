package com.altynai.internship.service;

import com.altynai.internship.dto.ChatRequestDto;
import com.altynai.internship.dto.ChatResponseDto;

public interface ChatService {
    ChatResponseDto getChatResponse(ChatRequestDto request);
}
