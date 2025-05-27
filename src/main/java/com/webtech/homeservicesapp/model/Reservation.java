package com.webtech.homeservicesapp.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "housing_id", nullable = false)
    private Housing housing;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate2;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate2;

    @Column(name = "guests", nullable = false)
    private int guests;

    @Column(name = "guest_count", nullable = false)
    private int guestCount;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.PENDING;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Housing getHousing() {
        return housing;
    }

    public void setHousing(Housing housing) {
        this.housing = housing;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public LocalDate getCheckInDate2() {
        return checkInDate2;
    }

    public void setCheckInDate2(LocalDate checkInDate2) {
        this.checkInDate2 = checkInDate2;
    }

    public LocalDate getCheckOutDate2() {
        return checkOutDate2;
    }

    public void setCheckOutDate2(LocalDate checkOutDate2) {
        this.checkOutDate2 = checkOutDate2;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public int getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    // ✅ New methods for JSP compatibility
    public Date getCheckInAsDate() {
        return checkInDate == null ? null : Date.from(checkInDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public Date getCheckOutAsDate() {
        return checkOutDate == null ? null : Date.from(checkOutDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}