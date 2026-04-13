package com.capgemini.hms.department.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * View controller for Department management pages.
 *
 * Client-side JS calls:
 *   GET    /api/v1/departments?page=&size=              → list all (paginated)
 *   GET    /api/v1/departments/{id}                     → single department
 *   GET    /api/v1/departments/{id}/physicians          → affiliated physician IDs
 *   POST   /api/v1/departments                          → create department (ADMIN)
 *   POST   /api/v1/departments/affiliate                → affiliate physician (ADMIN)
 *   PUT    /api/v1/departments/{id}                     → update department (ADMIN)
 *   PUT    /api/v1/departments/{id}/head?physicianId=   → set department head (ADMIN)
 *   DELETE /api/v1/departments/{id}                     → soft-delete (ADMIN)
 */
@Controller
@RequestMapping("/department")
public class DepartmentViewController {

    @GetMapping
    public String listDepartments() {
        return "department/list";
    }
}
