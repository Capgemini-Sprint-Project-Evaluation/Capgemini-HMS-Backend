package com.capgemini.hms.physician.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * View controller for Physician management pages.
 *
 * Client-side JS calls:
 *   GET    /api/v1/physicians?page=&size=&sort=            → list (paginated)
 *   GET    /api/v1/physicians/{id}                         → single physician
 *   GET    /api/v1/physicians/search?query=&page=&size=    → search
 *   POST   /api/v1/physicians                              → create (ADMIN)
 *   PUT    /api/v1/physicians/{id}                         → update (ADMIN)
 *   DELETE /api/v1/physicians/{id}                         → soft-delete (ADMIN)
 */
@Controller
@RequestMapping("/physician")
public class PhysicianViewController {

    @GetMapping
    public String listPhysicians() {
        return "physician/list";
    }
}
