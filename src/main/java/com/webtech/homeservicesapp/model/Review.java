package com.webtech.homeservicesapp.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer cleanlinessRating;

    @Column(nullable = false)
    private Integer locationRating;

    @Column(nullable = false)
    private Integer checkinExperienceRating;

    @Column(nullable = false)
    private Integer valueForMoneyRating;

    @Column(length = 1000)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "housing_id", nullable = false)
    private Housing housing;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCleanlinessRating() {
        return cleanlinessRating;
    }

    public void setCleanlinessRating(Integer cleanlinessRating) {
        this.cleanlinessRating = cleanlinessRating;
    }

    public Integer getLocationRating() {
        return locationRating;
    }

    public void setLocationRating(Integer locationRating) {
        this.locationRating = locationRating;
    }

    public Integer getCheckinExperienceRating() {
        return checkinExperienceRating;
    }

    public void setCheckinExperienceRating(Integer checkinExperienceRating) {
        this.checkinExperienceRating = checkinExperienceRating;
    }

    public Integer getValueForMoneyRating() {
        return valueForMoneyRating;
    }

    public void setValueForMoneyRating(Integer valueForMoneyRating) {
        this.valueForMoneyRating = valueForMoneyRating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
} 