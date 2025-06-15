package com.domuswap.controller;

import com.domuswap.model.Housing;
import com.domuswap.model.Reservation;
import com.domuswap.model.ReservationStatus;
import com.domuswap.model.User;
import com.domuswap.repository.HousingRepository;
import com.domuswap.repository.UserRepository;
import com.domuswap.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private HousingRepository housingRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/reservations")
    public String showUserReservations(@RequestParam Long userId, org.springframework.ui.Model model) {
        List<Reservation> userReservations = reservationService.getReservationsByUserId(userId);
        model.addAttribute("reservations", userReservations);
        return "reservations";
    }

    @GetMapping("/owner/reservations")
    public String showOwnerReservations(@RequestParam Long ownerId, org.springframework.ui.Model model) {
        List<Reservation> ownerReservations = reservationService.getReservationsByOwnerId(ownerId);
        model.addAttribute("reservations", ownerReservations);
        return "owner_reservations";
    }

    @PostMapping("/api/reservations")
    @ResponseBody
    public ResponseEntity<?> createReservation(@RequestBody Map<String, Object> payload) {
        try {
            Long housingId = Long.valueOf(payload.get("housingId").toString());
            Long userId = Long.valueOf(payload.get("userId").toString());
            LocalDate checkInDate = LocalDate.parse(payload.get("checkInDate").toString());
            LocalDate checkOutDate = LocalDate.parse(payload.get("checkOutDate").toString());
            Integer guestCount = Integer.valueOf(payload.get("guestCount").toString());

            Housing housing = housingRepository.findById(housingId)
                .orElseThrow(() -> new RuntimeException("Housing not found"));
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

            if (!reservationService.isAvailable(housingId, checkInDate, checkOutDate)) {
                return ResponseEntity.badRequest().body("Selected dates are not available");
            }

            Reservation reservation = new Reservation();
            reservation.setHousing(housing);
            reservation.setUser(user);
            reservation.setCheckInDate(checkInDate);
            reservation.setCheckOutDate(checkOutDate);
            reservation.setGuestCount(guestCount);
            reservation.setStatus(ReservationStatus.PENDING);

            Reservation savedReservation = reservationService.saveReservation(reservation);
            return ResponseEntity.ok(savedReservation);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating reservation: " + e.getMessage());
        }
    }

    @PostMapping("/api/reservations/{reservationId}/accept")
    @ResponseBody
    public ResponseEntity<?> acceptReservation(@PathVariable Long reservationId) {
        try {
            Reservation reservation = reservationService.getReservationById(reservationId);
            if (reservation == null) {
                return ResponseEntity.notFound().build();
            }
            reservation.setStatus(ReservationStatus.ACCEPTED);
            reservationService.saveReservation(reservation);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error accepting reservation: " + e.getMessage());
        }
    }

    @PostMapping("/api/reservations/{reservationId}/reject")
    @ResponseBody
    public ResponseEntity<?> rejectReservation(@PathVariable Long reservationId) {
        try {
            Reservation reservation = reservationService.getReservationById(reservationId);
            if (reservation == null) {
                return ResponseEntity.notFound().build();
            }
            reservation.setStatus(ReservationStatus.REJECTED);
            reservationService.saveReservation(reservation);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error rejecting reservation: " + e.getMessage());
        }
    }

    @PostMapping("/api/reservations/{reservationId}/complete")
    @ResponseBody
    public ResponseEntity<?> completeReservation(@PathVariable Long reservationId) {
        try {
            Reservation reservation = reservationService.getReservationById(reservationId);
            if (reservation == null) {
                return ResponseEntity.notFound().build();
            }
            reservation.setStatus(ReservationStatus.COMPLETED);
            reservationService.saveReservation(reservation);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error completing reservation: " + e.getMessage());
        }
    }

    @PostMapping("/api/reservations/{reservationId}/cancel")
    @ResponseBody
    public ResponseEntity<?> cancelReservation(@PathVariable Long reservationId) {
        try {
            Reservation reservation = reservationService.getReservationById(reservationId);
            if (reservation == null) {
                return ResponseEntity.notFound().build();
            }
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationService.saveReservation(reservation);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error canceling reservation: " + e.getMessage());
        }
    }

    @GetMapping("/api/reservations/housing/{housingId}")
    @ResponseBody
    public ResponseEntity<?> getReservationsByHousingId(@PathVariable Long housingId) {
        try {
            List<Reservation> reservations = reservationService.getReservationsByHousingId(housingId);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error getting reservations: " + e.getMessage());
        }
    }
} 