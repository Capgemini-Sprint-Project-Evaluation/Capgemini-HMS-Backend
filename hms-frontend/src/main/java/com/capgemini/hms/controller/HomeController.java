package com.capgemini.hms.controller;

import com.capgemini.hms.service.DeveloperService;
import com.capgemini.hms.service.EndpointService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final DeveloperService developerService;
    private final EndpointService endpointService;

    public HomeController(DeveloperService developerService, EndpointService endpointService) {
        this.developerService = developerService;
        this.endpointService = endpointService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("developers", developerService.getAllDevelopers());
        model.addAttribute("endpointCount", endpointService.getAllEndpoints().size());
        model.addAttribute("title", "Capgemini HMS - Distributed System Showcase");
        return "index";
    }
}
