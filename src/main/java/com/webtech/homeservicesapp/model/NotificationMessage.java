package com.webtech.homeservicesapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationMessage {
    @JsonProperty("type")
    private String type;        // e.g., "NEW_MESSAGE", "BOOKING_REQUEST"
    
    @JsonProperty("message")
    private String message;     // The notification content
    
    @JsonProperty("data")
    private Object data;        // Additional data if needed

    @JsonProperty("userId")
    private Long userId;        // The user ID to send the notification to

    public NotificationMessage() {
    }

    public NotificationMessage(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public NotificationMessage(String type, String message, Object data) {
        this.type = type;
        this.message = message;
        this.data = data;
    }

    public NotificationMessage(String type, String message, Object data, Long userId) {
        this.type = type;
        this.message = message;
        this.data = data;
        this.userId = userId;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "NotificationMessage{" +
                "type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", userId=" + userId +
                '}';
    }
} 