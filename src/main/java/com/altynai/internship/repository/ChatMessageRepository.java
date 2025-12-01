package com.altynai.internship.repository;

import com.altynai.internship.model.ChatMessage;
import com.altynai.internship.model.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Get all messages for a chat session
    List<ChatMessage> findBySessionOrderByTimestampAsc(ChatSession session);
}
