package com.altynai.internship.dto;

import lombok.*;
import lombok.Getter;

/**
 * Request DTO for sending a chat message.
 * sessionId may be null if user starts a new conversation.
 */
@Getter
@Setter
@NoArgsConstructor
public class ChatRequestDto {
    private String message;
    private Long sessionId; // null = create new session
}
