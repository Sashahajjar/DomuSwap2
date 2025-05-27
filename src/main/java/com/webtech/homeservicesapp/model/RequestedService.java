package com.webtech.homeservicesapp.model;

import javax.persistence.*;

@Entity
public class RequestedService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "housing_id")
    private Housing housing;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Housing getHousing() { return housing; }
    public void setHousing(Housing housing) { this.housing = housing; }
}
