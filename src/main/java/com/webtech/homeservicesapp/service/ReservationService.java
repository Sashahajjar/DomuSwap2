package com.webtech.homeservicesapp.service;

import com.webtech.homeservicesapp.model.Reservation;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    Reservation saveReservation(Reservation reservation);
    List<Reservation> getAllReservations();
    List<Reservation> getReservationsByUserId(Long userId);
    List<Reservation> getReservationsByOwnerId(Long ownerId);
    List<Reservation> getReservationsByHousingId(Long housingId);
    boolean isAvailable(Long housingId, LocalDate checkIn, LocalDate checkOut);
    Reservation getReservationById(Long id);
    void deleteReservation(Long reservationId);
}
