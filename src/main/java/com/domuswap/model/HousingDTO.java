package com.domuswap.model;

public class HousingDTO {
    public Long id;
    public String title;
    public String description;
    public String location;
    public String photo_1;
    public String imageUrl;
    public HousingDTO(Long id, String title, String description, String location, String photo_1) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.photo_1 = photo_1 == null ? "/images/logo.png" : 
                      (photo_1.startsWith("/uploads/") ? photo_1 : "/uploads/" + photo_1);
        this.imageUrl = this.photo_1;
    }
} 