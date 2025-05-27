package com.webtech.homeservicesapp.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "housing")
public class Housing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long housing_id;

    private String title;
    private String description;
    private String location;
    private String photo_1;
    private String photo_2;
    private String photo_3;
    private String constraint_text;
    private String amenities;
    
    @Column(name = "max_guests", nullable = false)
    private Integer maxGuests;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "housing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestedService> services = new ArrayList<>();

    // ——— Bean property for JSP (so you can do ${housing.id}) ———
    public Long getId() {
        return housing_id;
    }

    // ——— Convenience property for your imageUrl in JSP ———
    public String getImageUrl() {
        if (photo_1 == null) {
            return "/images/logo.png";
        }
        return photo_1.startsWith("/uploads/") ? photo_1 : "/uploads/" + photo_1;
    }

    public void setImageUrl(String imageUrl) {
        this.photo_1 = imageUrl;
    }

    // ——— Generated getters & setters ———

    public Long getHousing_id() {
        return housing_id;
    }

    public void setHousing_id(Long housing_id) {
        this.housing_id = housing_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoto_1() {
        return photo_1;
    }

    public void setPhoto_1(String photo_1) {
        this.photo_1 = photo_1;
    }

    public String getPhoto_2() {
        return photo_2;
    }

    public void setPhoto_2(String photo_2) {
        this.photo_2 = photo_2;
    }

    public String getPhoto_3() {
        return photo_3;
    }

    public void setPhoto_3(String photo_3) {
        this.photo_3 = photo_3;
    }

    public String getConstraint_text() {
        return constraint_text;
    }

    public void setConstraint_text(String constraint_text) {
        this.constraint_text = constraint_text;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<RequestedService> getServices() {
        return services;
    }

    public void setServices(List<RequestedService> services) {
        this.services = services;
    }

    public Integer getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(Integer maxGuests) {
        this.maxGuests = maxGuests;
    }
}
