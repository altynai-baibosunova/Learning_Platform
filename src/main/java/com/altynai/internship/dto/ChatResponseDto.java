package com.altynai.internship.dto;

import lombok.*;

/**
 * Response DTO for returning AI reply and current session ID.
 */
@Getter
@Setter
public class ChatResponseDto {
    private String reply;
    private Long sessionId;

    public ChatResponseDto() {}

    public ChatResponseDto(String reply) {
        this.reply = reply;
    }

    public ChatResponseDto(String reply, Long sessionId) {
        this.reply = reply;
        this.sessionId = sessionId;
    }
}

