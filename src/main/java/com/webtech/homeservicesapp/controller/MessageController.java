package com.webtech.homeservicesapp.controller;

import com.webtech.homeservicesapp.model.Message;
import com.webtech.homeservicesapp.model.MessageStatus;
import com.webtech.homeservicesapp.model.User;
import com.webtech.homeservicesapp.model.Reservation;
import com.webtech.homeservicesapp.model.NotificationMessage;
import com.webtech.homeservicesapp.repository.MessageRepository;
import com.webtech.homeservicesapp.repository.UserRepository;
import com.webtech.homeservicesapp.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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
            System.out.println("Received sendMessage request: " + request);
            User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
            User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

            // Check if there's a pending or accepted reservation between these users
            boolean hasValidReservation = reservationRepository.existsByHousingOwnerIdAndUserId(receiver.getId(), sender.getId()) ||
                                        reservationRepository.existsByHousingOwnerIdAndUserId(sender.getId(), receiver.getId());

            if (!hasValidReservation) {
                System.out.println("No valid reservation found between sender " + sender.getId() + " and receiver " + receiver.getId());
                return ResponseEntity.badRequest().body("No valid reservation found between these users");
            }

            Message message = new Message();
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setContent(request.getContent());
            message.setStatus(MessageStatus.PENDING);
            message.setCreatedAt(new java.util.Date());

            Message savedMessage = messageRepository.save(message);

            // === Send WebSocket notification to the owner ===
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Current principal: " + (auth != null ? auth.getName() : "null"));

            NotificationMessage notification = new NotificationMessage(
                "NEW_MESSAGE",
                "New message from " + sender.getName(),
                savedMessage,
                receiver.getId()
            );
            System.out.println("Created notification: " + notification);
            // Only send broadcast
            messagingTemplate.convertAndSend(
                "/topic/notifications",
                notification
            );
            System.out.println("WebSocket broadcast notification sent with payload: " + notification);
            // === End notification ===

            return ResponseEntity.ok(savedMessage);
        } catch (Exception e) {
            System.out.println("Error in sendMessage: " + e.getMessage());
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

    @GetMapping("/chat")
    public String showChat(@RequestParam(required = false) String userId, @RequestParam(required = false) String receiverId, Model model) {
        try {
            // Validate parameters
            if (userId == null || userId.trim().isEmpty()) {
                model.addAttribute("error", "User ID is required. Please log in again.");
                return "error";
            }
            if (receiverId == null || receiverId.trim().isEmpty()) {
                model.addAttribute("error", "Receiver ID is required. Please try again.");
                return "error";
            }

            // Parse IDs
            Long userIdLong;
            Long receiverIdLong;
            try {
                userIdLong = Long.parseLong(userId.trim());
                receiverIdLong = Long.parseLong(receiverId.trim());
            } catch (NumberFormatException e) {
                model.addAttribute("error", "Invalid user ID or receiver ID format. Please try again.");
                return "error";
            }

            // Get users
            User currentUser = userRepository.findById(userIdLong)
                .orElseThrow(() -> new RuntimeException("Current user not found. Please log in again."));
            User receiver = userRepository.findById(receiverIdLong)
                .orElseThrow(() -> new RuntimeException("Receiver not found. Please try again."));

            // Get messages between current user and receiver
            List<Message> messages = messageRepository.findBySenderIdAndReceiverIdOrderByCreatedAtDesc(userIdLong, receiverIdLong);
            messages.addAll(messageRepository.findByReceiverIdAndSenderIdOrderByCreatedAtDesc(userIdLong, receiverIdLong));
            messages.sort((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()));

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("receiver", receiver);
            model.addAttribute("messages", messages);

            return "chat";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
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
