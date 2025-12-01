package com.altynai.internship.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a single chat session between a user and the AI.
 * Each session contains multiple chat messages.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Optional title for the chat.
    // Usually generated from the first user message
    private String title;

    // Timestamp when this session was created
    private LocalDateTime createdAt;

    // Many chat sessions belong to one user
    @ManyToOne
    private User user;

    // One chat session contains many messages.
    // Cascade ensures messages are deleted if session is deleted
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages;
}
