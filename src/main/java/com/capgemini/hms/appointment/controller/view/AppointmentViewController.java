package com.capgemini.hms.appointment.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * View controller for Appointment management pages.
 *
 * Client-side JS calls:
 *   GET    /api/v1/appointments?page=&size=&sort=        → list all (paginated)
 *   GET    /api/v1/appointments/{id}                     → single appointment
 *   GET    /api/v1/appointments/physician/{id}           → physician schedule
 *   POST   /api/v1/appointments                          → book appointment
 *   PUT    /api/v1/appointments/{id}                     → update appointment
 *   DELETE /api/v1/appointments/{id}                     → cancel appointment
 */
@Controller
@RequestMapping("/appointment")
public class AppointmentViewController {

    @GetMapping
    public String listAppointments() {
        return "appointment/list";
    }
}
