package com.domuswap.controller;

import com.domuswap.model.Message;
import com.domuswap.model.User;
import com.domuswap.repository.MessageRepository;
import com.domuswap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/messages")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> payload, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to send messages");
        }

        try {
            Long receiverId = Long.valueOf(payload.get("receiverId").toString());
            String content = (String) payload.get("content");

            User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

            Message message = new Message();
            message.setSender(loggedInUser);
            message.setReceiver(receiver);
            message.setContent(content);
            message.setCreatedAt(new java.util.Date());
            message.setRead(false);

            messageRepository.save(message);
            return ResponseEntity.ok("Message sent successfully");
        } catch (Exception e) {
            logger.error("Error sending message", e);
            return ResponseEntity.badRequest().body("Error sending message: " + e.getMessage());
        }
    }

    @GetMapping("/conversation/{userId}")
    @ResponseBody
    public ResponseEntity<?> getConversation(@PathVariable Long userId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to view messages");
        }

        try {
            List<Message> messages = messageRepository.findBySenderIdAndReceiverIdOrderByCreatedAtAsc(loggedInUser.getId(), userId);
            messages.addAll(messageRepository.findByReceiverIdAndSenderIdOrderByCreatedAtAsc(loggedInUser.getId(), userId));
            messages.sort((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()));
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            logger.error("Error retrieving conversation", e);
            return ResponseEntity.badRequest().body("Error retrieving conversation: " + e.getMessage());
        }
    }

    @GetMapping("/unread")
    @ResponseBody
    public ResponseEntity<?> getUnreadMessages(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to view messages");
        }

        try {
            List<Message> unreadMessages = messageRepository.findByReceiverIdAndReadFalseOrderByCreatedAtDesc(loggedInUser.getId());
            return ResponseEntity.ok(unreadMessages);
        } catch (Exception e) {
            logger.error("Error retrieving unread messages", e);
            return ResponseEntity.badRequest().body("Error retrieving unread messages: " + e.getMessage());
        }
    }

    @PostMapping("/mark-read/{messageId}")
    @ResponseBody
    public ResponseEntity<?> markMessageAsRead(@PathVariable Long messageId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to mark messages as read");
        }

        try {
            Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

            if (!message.getReceiver().getId().equals(loggedInUser.getId())) {
                return ResponseEntity.badRequest().body("You can only mark your own messages as read");
            }

            message.setRead(true);
            messageRepository.save(message);
            return ResponseEntity.ok("Message marked as read");
        } catch (Exception e) {
            logger.error("Error marking message as read", e);
            return ResponseEntity.badRequest().body("Error marking message as read: " + e.getMessage());
        }
    }
} 