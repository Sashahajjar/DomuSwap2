package com.domuswap.model;

public class UserDTO {
    private String name;
    private String email;
    private String role;
    public UserDTO(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
} 