package com.domuswap.controller;

import com.domuswap.model.Notification;
import com.domuswap.model.User;
import com.domuswap.repository.NotificationRepository;
import com.domuswap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/notifications")
public class NotificationController {
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<?> getUserNotifications(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to view notifications");
        }

        try {
            List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(loggedInUser);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            logger.error("Error retrieving notifications", e);
            return ResponseEntity.badRequest().body("Error retrieving notifications: " + e.getMessage());
        }
    }

    @PostMapping("/mark-read/{notificationId}")
    @ResponseBody
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long notificationId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to mark notifications as read");
        }

        try {
            Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

            if (!notification.getUser().getId().equals(loggedInUser.getId())) {
                return ResponseEntity.badRequest().body("You can only mark your own notifications as read");
            }

            notification.setRead(true);
            notificationRepository.save(notification);
            return ResponseEntity.ok("Notification marked as read");
        } catch (Exception e) {
            logger.error("Error marking notification as read", e);
            return ResponseEntity.badRequest().body("Error marking notification as read: " + e.getMessage());
        }
    }

    @PostMapping("/mark-all-read")
    @ResponseBody
    public ResponseEntity<?> markAllNotificationsAsRead(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to mark notifications as read");
        }

        try {
            List<Notification> notifications = notificationRepository.findByUserAndReadFalse(loggedInUser);
            for (Notification notification : notifications) {
                notification.setRead(true);
            }
            notificationRepository.saveAll(notifications);
            return ResponseEntity.ok("All notifications marked as read");
        } catch (Exception e) {
            logger.error("Error marking all notifications as read", e);
            return ResponseEntity.badRequest().body("Error marking all notifications as read: " + e.getMessage());
        }
    }

    @GetMapping("/unread-count")
    @ResponseBody
    public ResponseEntity<?> getUnreadNotificationCount(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body("User must be logged in to get notification count");
        }

        try {
            long count = notificationRepository.countByUserAndReadFalse(loggedInUser);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            logger.error("Error getting unread notification count", e);
            return ResponseEntity.badRequest().body("Error getting unread notification count: " + e.getMessage());
        }
    }
} 