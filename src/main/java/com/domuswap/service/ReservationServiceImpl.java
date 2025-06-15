package com.domuswap.service;

import com.domuswap.model.Reservation;
import com.domuswap.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Override
    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    @Override
    public List<Reservation> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }
    @Override
    public List<Reservation> getReservationsByOwnerId(Long ownerId) {
        return reservationRepository.findByHousingOwnerId(ownerId);
    }
    @Override
    public List<Reservation> getReservationsByHousingId(Long housingId) {
        return reservationRepository.findByHousingId(housingId);
    }
    @Override
    public boolean isAvailable(Long housingId, LocalDate checkIn, LocalDate checkOut) {
        List<Reservation> existingReservations = reservationRepository.findByHousingId(housingId);
        return existingReservations.stream().noneMatch(reservation ->
            (checkIn.isBefore(reservation.getCheckOutDate()) &&
             checkOut.isAfter(reservation.getCheckInDate()))
        );
    }
    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
} 