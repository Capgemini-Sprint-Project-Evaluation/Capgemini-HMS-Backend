package com.capgemini.hms.stay.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * View controller for Patient Stay (Admission/Discharge) management.
 *
 * Client-side JS calls:
 *   GET    /api/v1/stays?page=&size=&sort=   → all stays (paginated)
 *   GET    /api/v1/stays/active              → currently admitted patients
 *   GET    /api/v1/stays/{id}               → single stay
 *   POST   /api/v1/stays                    → check-in patient (ADMIN, NURSE)
 *   PUT    /api/v1/stays/{id}?notes=        → update / discharge patient (ADMIN, NURSE)
 *   DELETE /api/v1/stays/{id}               → soft-delete stay (ADMIN)
 */
@Controller
@RequestMapping("/stay")
public class StayViewController {

    @GetMapping
    public String listStays() {
        return "stay/list";
    }
}
