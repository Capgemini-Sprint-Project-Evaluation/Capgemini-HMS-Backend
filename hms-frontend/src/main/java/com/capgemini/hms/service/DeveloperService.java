package com.capgemini.hms.service;

import com.capgemini.hms.model.Developer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DeveloperService {

        private final List<Developer> developers = new ArrayList<>();

        public DeveloperService() {
                // Akash is now FIRST as requested
                developers.add(Developer.builder()
                                .id("akash")
                                .name("Akash Gaikwad")
                                .role("Auth & Systems Lead")
                                .domain("Security & Core Domain")
                                .description("Handles system core, authentication, security policy, and hospital infrastructure management (Departments & Dashboards).")
                                .photoUrl("/img/avatar-akash.png")
                                .responsibilities(Arrays.asList("Identity Management", "Security Policy",
                                                "Department Analytics", "System Stats"))
                                .build());

                developers.add(Developer.builder()
                                .id("rahul")
                                .name("Rahul Morya")
                                .role("Scheduling Domain Lead")
                                .domain("Appointment & Scheduling")
                                .description("Handles booking flow APIs, physician management, and clinical availability logic.")
                                .photoUrl("/img/avatar-rahul.png")
                                .responsibilities(Arrays.asList("Appointment API", "Physician Registry",
                                                "OnCall Scheduling"))
                                .build());

                developers.add(Developer.builder()
                                .id("ashutosh")
                                .name("Ashutosh Ranjan")
                                .role("Patient Domain Lead")
                                .domain("Patient Management")
                                .description("Handles patient profiles and their lifecycle within the hospital including stays and procedures.")
                                .photoUrl("/img/avatar-ashutosh.png")
                                .responsibilities(Arrays.asList("Patient Profile API", "Stay Records",
                                                "Medical Procedures"))
                                .build());

                developers.add(Developer.builder()
                                .id("aditya-reddy")
                                .name("Aditya Reddy")
                                .role("Clinical Domain Lead")
                                .domain("Medication & Prescription")
                                .description("Handles treatment-related APIs, medication catalogs, and patient prescriptions.")
                                .photoUrl("/img/avatar-aditya-reddy.png")
                                .responsibilities(Arrays.asList("Medication Catalog", "Prescription API"))
                                .build());

                developers.add(Developer.builder()
                                .id("ayush")
                                .name("Ayush Yede")
                                .role("Infrastructure Domain Lead")
                                .domain("Room & Infrastructure")
                                .description("Handles physical hospital infrastructure, room allocation, and real-time availability.")
                                .photoUrl("/img/avatar-ayush.png")
                                .responsibilities(Arrays.asList("Room Availability", "Block Management"))
                                .build());

                developers.add(Developer.builder()
                                .id("aditya-jadhav")
                                .name("Aditya Jadhav")
                                .role("Care Domain Lead")
                                .domain("Nursing & Care Operations")
                                .description("Handles nurse assignments, shift operations, and patient care workflows.")
                                .photoUrl("/img/avatar-aditya.png")
                                .responsibilities(Arrays.asList("Nurse Registry", "OnCall Nursing"))
                                .build());
        }

        public List<Developer> getAllDevelopers() {
                return developers;
        }

        public Developer getDeveloperById(String id) {
                return developers.stream()
                                .filter(d -> d.getId().equalsIgnoreCase(id))
                                .findFirst()
                                .orElse(null);
        }
}
