package com.webapp.homeservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
 
@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration.class
})
public class HomeServicesAppApplication {
} 