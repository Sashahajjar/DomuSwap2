package com.webtech.homeservicesapp.controller;

import com.webtech.homeservicesapp.model.NotificationMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Endpoint to send notifications to specific users
    @PostMapping("/api/notifications/send")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationMessage notification) {
        try {
            System.out.println("Received notification request: " + notification);
            
            if (notification.getUserId() == null) {
                return ResponseEntity.badRequest().body("User ID is required");
            }
            
            System.out.println("Sending to user: " + notification.getUserId());
            
            // Send to specific user's topic
            messagingTemplate.convertAndSendToUser(
                notification.getUserId().toString(),
                "/topic/notifications",
                notification
            );
            System.out.println("Notification sent successfully");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("Error sending notification: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Error sending notification: " + e.getMessage());
        }
    }

    // WebSocket endpoint for clients to subscribe to their notifications
    @MessageMapping("/notifications")
    @SendTo("/topic/notifications")
    public NotificationMessage handleNotification(NotificationMessage notification) {
        System.out.println("Handling WebSocket notification: " + notification);
        return notification;
    }
} 