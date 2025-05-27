package com.webtech.homeservicesapp.model;

public enum ReservationStatus {
    PENDING,    // Initial state when booking is requested
    ACCEPTED,   // Owner has accepted the booking
    REJECTED,   // Owner has rejected the booking
    COMPLETED,  // Stay has been completed
    CANCELLED   // Booking was cancelled
} 