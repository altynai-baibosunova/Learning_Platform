package com.altynai.internship.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Represents a single message inside a chat session.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Message text (from user or AI)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // Timestamp of the message
    private LocalDateTime timestamp;

    // Sender type: USER or AI
    @Enumerated(EnumType.STRING)
    private SenderRole sender;

    // Many messages belong to one chat session.
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatSession session;
}
