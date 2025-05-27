package com.webtech.homeservicesapp.controller;

import com.webtech.homeservicesapp.model.Message;
import com.webtech.homeservicesapp.model.MessageStatus;
import com.webtech.homeservicesapp.model.User;
import com.webtech.homeservicesapp.model.Reservation;
import com.webtech.homeservicesapp.repository.MessageRepository;
import com.webtech.homeservicesapp.repository.UserRepository;
import com.webtech.homeservicesapp.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/client-messages")
    public String showClientMessages(@RequestParam Long ownerId, Model model) {
        // Get all reservations for the owner's properties
        List<Reservation> reservations = reservationRepository.findByHousingOwnerId(ownerId);
        model.addAttribute("reservations", reservations);

        // Get all messages for the owner
        List<Message> messages = messageRepository.findByReceiverIdOrderByCreatedAtDesc(ownerId);
        model.addAttribute("messages", messages);

        return "client_messages";
    }

    @GetMapping("/api/messages/{userId}")
    @ResponseBody
    public ResponseEntity<?> getMessages(@PathVariable Long userId, @RequestParam Long ownerId) {
        try {
            List<Message> messages = messageRepository.findBySenderIdAndReceiverIdOrderByCreatedAtDesc(userId, ownerId);
            messages.addAll(messageRepository.findByReceiverIdAndSenderIdOrderByCreatedAtDesc(userId, ownerId));
            messages.sort((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()));
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching messages: " + e.getMessage());
        }
    }

    @PostMapping("/api/messages/send")
    @ResponseBody
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequest request) {
        try {
            User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
            User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

            Message message = new Message();
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setContent(request.getContent());
            message.setStatus(MessageStatus.PENDING);

            messageRepository.save(message);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending message: " + e.getMessage());
        }
    }

    @PostMapping("/api/messages/{messageId}/accept")
    @ResponseBody
    public ResponseEntity<?> acceptMessage(@PathVariable Long messageId) {
        try {
            Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
            
            message.setStatus(MessageStatus.ACCEPTED);
            messageRepository.save(message);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error accepting message: " + e.getMessage());
        }
    }

    @PostMapping("/api/messages/{messageId}/reject")
    @ResponseBody
    public ResponseEntity<?> rejectMessage(@PathVariable Long messageId) {
        try {
            Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
            
            message.setStatus(MessageStatus.REJECTED);
            messageRepository.save(message);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error rejecting message: " + e.getMessage());
        }
    }
}

class MessageRequest {
    private Long senderId;
    private Long receiverId;
    private String content;

    // Getters and setters
    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
