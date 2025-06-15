package com.webtech.homeservicesapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = Paths.get(System.getProperty("user.dir"), "uploads").toAbsolutePath().toString();
        System.out.println("Configuring resource handler for uploads directory: " + uploadDir);
        
        // Verify the upload directory exists and is readable
        if (Files.exists(Paths.get(uploadDir))) {
            System.out.println("Upload directory exists");
            System.out.println("Directory is readable: " + Files.isReadable(Paths.get(uploadDir)));
            try {
                System.out.println("Directory permissions: " + Files.getPosixFilePermissions(Paths.get(uploadDir)));
            } catch (Exception e) {
                System.out.println("Could not get directory permissions: " + e.getMessage());
            }
        } else {
            System.out.println("WARNING: Upload directory does not exist!");
        }
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/")
                .setCachePeriod(0)  // Disable caching for development
                .resourceChain(false);  // Disable resource chain for development
        
        System.out.println("Resource handler configured for /uploads/** -> " + uploadDir);
    }
}
