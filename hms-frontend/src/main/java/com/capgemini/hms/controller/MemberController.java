package com.capgemini.hms.controller;

import com.capgemini.hms.model.Developer;
import com.capgemini.hms.model.Endpoint;
import com.capgemini.hms.service.DeveloperService;
import com.capgemini.hms.service.EndpointService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MemberController {

    private final DeveloperService developerService;
    private final EndpointService endpointService;

    public MemberController(DeveloperService developerService, EndpointService endpointService) {
        this.developerService = developerService;
        this.endpointService = endpointService;
    }

    @GetMapping("/member/{id}")
    public String memberDetail(@PathVariable String id, Model model) {
        Developer developer = developerService.getDeveloperById(id);
        if (developer == null) {
            return "redirect:/";
        }

        List<Endpoint> developerEndpoints = endpointService.getEndpointsByDeveloper(id);
        model.addAttribute("developer", developer);
        model.addAttribute("endpoints", developerEndpoints);
        model.addAttribute("title", developer.getName() + " - Domain Endpoints");
        return "member";
    }

    @GetMapping("/endpoint/{id}")
    public String endpointDetail(@PathVariable String id, Model model) {
        Endpoint endpoint = endpointService.getEndpointById(id);
        if (endpoint == null) {
            return "redirect:/";
        }

        Developer developer = developerService.getDeveloperById(endpoint.getDeveloperId());
        if (developer == null) {
            return "redirect:/";
        }
        model.addAttribute("endpoint", endpoint);
        model.addAttribute("developer", developer);
        model.addAttribute("title", "Test API: " + endpoint.getName());
        return "endpoint";
    }
}
