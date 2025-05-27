package com.webtech.homeservicesapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.webtech.homeservicesapp.model.Housing;
import com.webtech.homeservicesapp.repository.HousingRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {
	
	
	@Autowired
	private HousingRepository housingRepository;


	@GetMapping("/listing")
	public String showListingPage() {
	    return "listing"; // Spring will resolve to /WEB-INF/views/listing.jsp
	}


    @GetMapping("/customer")
    public String showCustomerPage() {
        return "customer"; // maps to templates/customer.html (case-sensitive)
    }

    @GetMapping("/client_messages")
    public String showClientMessages() {
        return "client_messages"; // maps to /WEB-INF/views/client_messages.jsp
    }

    @GetMapping("/main_page")
    public String showMainPage() {
    	return "redirect:/listing?success"; // if you also want to handle the logo redirect
    }
}
