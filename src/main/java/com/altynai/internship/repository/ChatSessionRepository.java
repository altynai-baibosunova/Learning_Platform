package com.altynai.internship.repository;

import com.altynai.internship.model.ChatSession;
import com.altynai.internship.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

    // Get all chat sessions for a specific user
    List<ChatSession> findByUserOrderByCreatedAtDesc(User user);
}
