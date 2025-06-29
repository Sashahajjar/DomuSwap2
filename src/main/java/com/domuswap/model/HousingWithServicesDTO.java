package com.domuswap.model;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class HousingWithServicesDTO {
    private String title;
    private String description;
    private String location;
    private String photo_1;
    private String photo_2;
    private String photo_3;
    private String constraint_text;
    private Long ownerId;
    private List<String> services;
    private String constraintText;
    private String imagePath;
    private String amenities;
    private Integer maxGuests;
    private MultipartFile imageFile;
    private MultipartFile image1;
    private MultipartFile image2;
    private MultipartFile image3;
    public MultipartFile getImageFile() { return imageFile; }
    public void setImageFile(MultipartFile imageFile) { this.imageFile = imageFile; }
    public String getConstraintText() { return constraintText; }
    public void setConstraintText(String constraintText) { this.constraintText = constraintText; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getPhoto_1() { return photo_1; }
    public void setPhoto_1(String photo_1) { this.photo_1 = photo_1; }
    public String getPhoto_2() { return photo_2; }
    public void setPhoto_2(String photo_2) { this.photo_2 = photo_2; }
    public String getPhoto_3() { return photo_3; }
    public void setPhoto_3(String photo_3) { this.photo_3 = photo_3; }
    public String getConstraint_text() { return constraint_text; }
    public void setConstraint_text(String constraint_text) { this.constraint_text = constraint_text; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public List<String> getServices() { return services; }
    public void setServices(List<String> services) { this.services = services; }
    public Integer getMaxGuests() { return maxGuests; }
    public void setMaxGuests(Integer maxGuests) { this.maxGuests = maxGuests; }
    public MultipartFile getImage1() { return image1; }
    public void setImage1(MultipartFile image1) { this.image1 = image1; }
    public MultipartFile getImage2() { return image2; }
    public void setImage2(MultipartFile image2) { this.image2 = image2; }
    public MultipartFile getImage3() { return image3; }
    public void setImage3(MultipartFile image3) { this.image3 = image3; }
} 