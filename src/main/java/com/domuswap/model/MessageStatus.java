package com.domuswap.model;

public enum MessageStatus {
    PENDING,    // Initial state when message is sent
    ACCEPTED,   // Owner has accepted the message/chat
    REJECTED    // Owner has rejected the message/chat
} 