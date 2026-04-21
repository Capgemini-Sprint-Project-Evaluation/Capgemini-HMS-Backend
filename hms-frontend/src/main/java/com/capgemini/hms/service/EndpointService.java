package com.capgemini.hms.service;

import com.capgemini.hms.model.Endpoint;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EndpointService {

    private final List<Endpoint> endpoints = new ArrayList<>();

    public EndpointService() {
        register("akash-auth-01", "System Login", "POST", "/api/v1/auth/signin",
                "Authenticate a user and start an HttpOnly session.", "Public", "akash", "Auth",
                fields("username", "string", "password", "password"));
        register("akash-auth-02", "User Registration", "POST", "/api/v1/auth/signup",
                "Create a new system user with a requested role.", "Public", "akash", "Auth",
                fields("username", "string", "email", "string", "password", "password", "role", "string"));
        register("akash-auth-03", "System Logout", "POST", "/api/v1/auth/logout",
                "Invalidate the current session and clear the security context.", "Authenticated", "akash", "Auth",
                fields());

        register("akash-mgmt-01", "List Departments", "GET", "/api/v1/management/departments",
                "Fetch all active hospital departments.", "ADMIN/DOCTOR/NURSE", "akash", "Management", fields());
        register("akash-mgmt-02", "Get Department By ID", "GET", "/api/v1/management/departments/{id}",
                "Fetch one active department by identifier.", "ADMIN/DOCTOR/NURSE", "akash", "Management",
                fields("id", "number"));
        register("akash-mgmt-03", "Create Department", "POST", "/api/v1/management/departments",
                "Create a new department record.", "ADMIN", "akash", "Management",
                fields("departmentId", "number", "name", "string", "headId", "number"));
        register("akash-mgmt-04", "Update Department", "PUT", "/api/v1/management/departments/{id}",
                "Update department metadata.", "ADMIN", "akash", "Management",
                fields("id", "number", "name", "string", "headId", "number"));
        register("akash-mgmt-05", "Delete Department", "DELETE", "/api/v1/management/departments/{id}",
                "Soft-delete a department record.", "ADMIN", "akash", "Management",
                fields("id", "number"));

        register("akash-admin-01", "Dashboard Summary", "GET", "/api/v1/admin/dashboard/summary",
                "Return top-level hospital KPIs for the admin dashboard.", "ADMIN", "akash", "Admin", fields());
        register("akash-admin-02", "Department Statistics", "GET", "/api/v1/admin/dashboard/departments",
                "Return physician counts per active department.", "ADMIN", "akash", "Admin", fields());
        register("akash-admin-03", "Department Affiliations", "GET", "/api/v1/admin/departments/{id}/physicians",
                "List physician affiliations for a department.", "ADMIN/DOCTOR/NURSE", "akash", "Admin",
                fields("id", "number"));
        register("akash-admin-04", "Create Affiliation", "POST", "/api/v1/admin/departments/affiliate",
                "Affiliate a physician with a department.", "ADMIN", "akash", "Admin",
                fields("physicianId", "number", "departmentId", "number", "primaryAffiliation", "boolean"));
        register("akash-admin-05", "List Users", "GET", "/api/v1/admin/users",
                "Fetch all registered system users.", "ADMIN", "akash", "Security", fields());
        register("akash-admin-06", "Audit Logs", "GET", "/api/v1/admin/audit/logs",
                "Fetch audit log entries for administrative review.", "ADMIN", "akash", "Security", fields());

        register("ashutosh-patient-01", "List Patients", "GET", "/api/v1/patient",
                "Fetch all active patients.", "ADMIN/DOCTOR/NURSE", "ashutosh", "Patient", fields());
        register("ashutosh-patient-02", "Search Patients", "GET", "/api/v1/patient/search",
                "Search patients by name, address, phone, or insurance ID.", "ADMIN/DOCTOR/NURSE", "ashutosh", "Patient",
                fields("query", "string"));
        register("ashutosh-patient-03", "Get Patient By SSN", "GET", "/api/v1/patient/{ssn}",
                "Fetch a patient record by SSN.", "ADMIN/DOCTOR/NURSE", "ashutosh", "Patient",
                fields("ssn", "number"));
        register("ashutosh-patient-04", "Create Patient", "POST", "/api/v1/patient",
                "Register a new patient record.", "ADMIN/NURSE", "ashutosh", "Patient",
                fields("ssn", "number", "name", "string", "address", "string", "phone", "string", "insuranceId", "string", "pcpId", "number"));
        register("ashutosh-patient-05", "Update Patient", "PUT", "/api/v1/patient/{ssn}",
                "Update patient demographic details.", "ADMIN/NURSE", "ashutosh", "Patient",
                fields("ssn", "number", "name", "string", "address", "string", "phone", "string", "insuranceId", "string", "pcpId", "number"));
        register("ashutosh-patient-06", "Delete Patient", "DELETE", "/api/v1/patient/{ssn}",
                "Soft-delete a patient record.", "ADMIN", "ashutosh", "Patient",
                fields("ssn", "number"));
        register("ashutosh-stay-01", "Patient Stay History", "GET", "/api/v1/patient/{ssn}/stays",
                "Fetch all stays for a patient.", "ADMIN/DOCTOR/NURSE", "ashutosh", "Stay",
                fields("ssn", "number"));
        register("ashutosh-stay-02", "Active Stays", "GET", "/api/v1/patient/stays/active",
                "Fetch all currently active stays.", "ADMIN/DOCTOR/NURSE", "ashutosh", "Stay", fields());
        register("ashutosh-stay-03", "Update Stay", "PUT", "/api/v1/patient/stays/{id}",
                "Update stay timing details.", "ADMIN/DOCTOR", "ashutosh", "Stay",
                fields("id", "number", "patientSsn", "number", "roomNumber", "number", "startTime", "datetime", "endTime", "datetime"));
        register("ashutosh-stay-04", "Create Stay", "POST", "/api/v1/patient/stays",
                "Admit a patient and create a new stay record.", "ADMIN/NURSE", "ashutosh", "Stay",
                fields("patientSsn", "number", "roomNumber", "number", "startTime", "datetime", "notes", "string"));
        register("ashutosh-procedure-01", "List Procedures", "GET", "/api/v1/patient/procedures",
                "Fetch the medical procedure catalog.", "ADMIN/DOCTOR/NURSE", "ashutosh", "Procedure", fields());
        register("ashutosh-procedure-02", "Search Procedures", "GET", "/api/v1/patient/procedures/search",
                "Search the procedure catalog.", "ADMIN/DOCTOR/NURSE", "ashutosh", "Procedure",
                fields("query", "string"));
        register("ashutosh-procedure-03", "Patient Procedures", "GET", "/api/v1/patient/{ssn}/procedures",
                "Fetch all procedures undergone by a patient.", "ADMIN/DOCTOR/NURSE", "ashutosh", "Procedure",
                fields("ssn", "number"));
        register("ashutosh-procedure-04", "Create Procedure", "POST", "/api/v1/patient/procedures",
                "Create a new procedure catalog entry.", "ADMIN", "ashutosh", "Procedure",
                fields("code", "number", "name", "string", "cost", "number"));
        register("ashutosh-undergoes-01", "Undergoes History", "GET", "/api/v1/patient/undergoes/{ssn}",
                "Fetch all procedure records for a patient.", "ADMIN/DOCTOR/NURSE", "ashutosh", "Undergoes",
                fields("ssn", "number"));
        register("ashutosh-undergoes-02", "Record Procedure", "POST", "/api/v1/patient/undergoes",
                "Record a procedure completed during a stay.", "ADMIN/DOCTOR", "ashutosh", "Undergoes",
                fields("patientSsn", "number", "procedureCode", "number", "stayId", "number", "physicianId", "number", "date", "datetime"));

        register("aditya-reddy-med-01", "List Medications", "GET", "/api/v1/clinical/medications",
                "Fetch the medication catalog.", "ADMIN/DOCTOR/NURSE", "aditya-reddy", "Medication", fields());
        register("aditya-reddy-med-02", "Search Medications", "GET", "/api/v1/clinical/medications/search",
                "Search medications by name or brand.", "ADMIN/DOCTOR/NURSE", "aditya-reddy", "Medication",
                fields("query", "string"));
        register("aditya-reddy-med-03", "Get Medication By Code", "GET", "/api/v1/clinical/medications/{code}",
                "Fetch a single medication by code.", "ADMIN/DOCTOR/NURSE", "aditya-reddy", "Medication",
                fields("code", "number"));
        register("aditya-reddy-med-04", "Create Medication", "POST", "/api/v1/clinical/medications",
                "Create a medication catalog entry.", "ADMIN", "aditya-reddy", "Medication",
                fields("code", "number", "name", "string", "brand", "string", "description", "string"));
        register("aditya-reddy-med-05", "Update Medication", "PUT", "/api/v1/clinical/medications/{code}",
                "Update a medication catalog entry.", "ADMIN", "aditya-reddy", "Medication",
                fields("code", "number", "name", "string", "brand", "string", "description", "string"));
        register("aditya-reddy-med-06", "Delete Medication", "DELETE", "/api/v1/clinical/medications/{code}",
                "Soft-delete a medication catalog entry.", "ADMIN", "aditya-reddy", "Medication",
                fields("code", "number"));
        register("aditya-reddy-pres-01", "Create Prescription", "POST", "/api/v1/clinical/prescriptions",
                "Issue a prescription for a patient.", "ADMIN/DOCTOR", "aditya-reddy", "Prescription",
                fields("physicianId", "number", "patientSsn", "number", "medicationCode", "number", "date", "datetime", "dose", "string", "appointmentId", "number"));
        register("aditya-reddy-pres-02", "Patient Prescriptions", "GET", "/api/v1/clinical/prescriptions/patient/{ssn}",
                "Fetch prescriptions for a patient.", "ADMIN/DOCTOR/NURSE", "aditya-reddy", "Prescription",
                fields("ssn", "number"));

        register("ayush-infra-01", "List Rooms", "GET", "/api/v1/infrastructure/rooms",
                "Fetch all hospital rooms.", "ADMIN", "ayush", "Infrastructure", fields());
        register("ayush-infra-02", "Available Rooms", "GET", "/api/v1/infrastructure/rooms/available",
                "Fetch available rooms for admission.", "ADMIN/NURSE", "ayush", "Infrastructure", fields());
        register("ayush-infra-03", "Create Room", "POST", "/api/v1/infrastructure/rooms",
                "Create a room record.", "ADMIN", "ayush", "Infrastructure",
                fields("roomNumber", "number", "roomType", "string", "blockFloor", "number", "blockCode", "number", "unavailable", "boolean"));
        register("ayush-infra-04", "Update Room", "PUT", "/api/v1/infrastructure/rooms/{id}",
                "Update room metadata and availability.", "ADMIN", "ayush", "Infrastructure",
                fields("id", "number", "roomNumber", "number", "roomType", "string", "blockFloor", "number", "blockCode", "number", "unavailable", "boolean"));
        register("ayush-infra-05", "Delete Room", "DELETE", "/api/v1/infrastructure/rooms/{id}",
                "Soft-delete a room record.", "ADMIN", "ayush", "Infrastructure",
                fields("id", "number"));
        register("ayush-infra-06", "List Blocks", "GET", "/api/v1/infrastructure/blocks",
                "Fetch all hospital blocks.", "ADMIN/NURSE", "ayush", "Infrastructure", fields());

        register("aditya-nursing-01", "List Nurses", "GET", "/api/v1/nursing/nurses",
                "Fetch all active nurses.", "ADMIN/DOCTOR/NURSE", "aditya-jadhav", "Nursing", fields());
        register("aditya-nursing-02", "Search Nurses", "GET", "/api/v1/nursing/nurses/search",
                "Search nurses by name or position.", "ADMIN/DOCTOR/NURSE", "aditya-jadhav", "Nursing",
                fields("query", "string"));
        register("aditya-nursing-03", "Get Nurse By ID", "GET", "/api/v1/nursing/nurses/{id}",
                "Fetch a single nurse profile.", "ADMIN/DOCTOR/NURSE", "aditya-jadhav", "Nursing",
                fields("id", "number"));
        register("aditya-nursing-04", "Create Nurse", "POST", "/api/v1/nursing/nurses",
                "Create a nurse profile.", "ADMIN", "aditya-jadhav", "Nursing",
                fields("employeeId", "number", "name", "string", "position", "string", "registered", "boolean", "ssn", "number"));
        register("aditya-nursing-05", "Update Nurse", "PUT", "/api/v1/nursing/nurses/{id}",
                "Update an existing nurse profile.", "ADMIN", "aditya-jadhav", "Nursing",
                fields("id", "number", "employeeId", "number", "name", "string", "position", "string", "registered", "boolean", "ssn", "number"));
        register("aditya-nursing-06", "Delete Nurse", "DELETE", "/api/v1/nursing/nurses/{id}",
                "Soft-delete a nurse profile.", "ADMIN", "aditya-jadhav", "Nursing",
                fields("id", "number"));
        register("aditya-nursing-07", "On-Call Rotation", "GET", "/api/v1/nursing/on-call",
                "Fetch the current nurse on-call rotation.", "ADMIN/DOCTOR/NURSE", "aditya-jadhav", "Nursing", fields());

        register("rahul-scheduling-01", "List Physicians", "GET", "/api/v1/scheduling/physicians",
                "Fetch all active physicians.", "Authenticated", "rahul", "Scheduling", fields());
        register("rahul-scheduling-02", "Search Physicians", "GET", "/api/v1/scheduling/physicians/search",
                "Search physicians by name or position.", "Authenticated", "rahul", "Scheduling",
                fields("query", "string"));
        register("rahul-scheduling-03", "Get Physician By ID", "GET", "/api/v1/scheduling/physicians/{id}",
                "Fetch a physician profile.", "Authenticated", "rahul", "Scheduling",
                fields("id", "number"));
        register("rahul-scheduling-04", "Create Physician", "POST", "/api/v1/scheduling/physicians",
                "Create a physician profile.", "ADMIN", "rahul", "Scheduling",
                fields("employeeId", "number", "name", "string", "position", "string", "ssn", "number"));
        register("rahul-scheduling-05", "Update Physician", "PUT", "/api/v1/scheduling/physicians/{id}",
                "Update an existing physician profile.", "ADMIN", "rahul", "Scheduling",
                fields("id", "number", "employeeId", "number", "name", "string", "position", "string", "ssn", "number"));
        register("rahul-scheduling-06", "Delete Physician", "DELETE", "/api/v1/scheduling/physicians/{id}",
                "Soft-delete a physician profile.", "ADMIN", "rahul", "Scheduling",
                fields("id", "number"));
        register("rahul-scheduling-07", "Physician Certifications", "GET", "/api/v1/scheduling/physicians/{id}/trained-in",
                "Fetch a physician's certifications.", "ADMIN/DOCTOR/NURSE", "rahul", "Scheduling",
                fields("id", "number"));
        register("rahul-scheduling-08", "Create Certification", "POST", "/api/v1/scheduling/physicians/trained-in",
                "Register a physician certification.", "ADMIN", "rahul", "Scheduling",
                fields("physicianId", "number", "procedureCode", "number", "treatmentDate", "datetime", "certificationExpires", "datetime"));
        register("rahul-scheduling-09", "List Appointments", "GET", "/api/v1/scheduling/appointments",
                "Fetch all active appointments.", "ADMIN/DOCTOR/NURSE", "rahul", "Scheduling", fields());
        register("rahul-scheduling-10", "Search Appointments", "GET", "/api/v1/scheduling/appointments/search",
                "Search appointments by examination room.", "ADMIN/DOCTOR/NURSE", "rahul", "Scheduling",
                fields("room", "string"));
        register("rahul-scheduling-11", "Create Appointment", "POST", "/api/v1/scheduling/appointments",
                "Create a new appointment booking.", "ADMIN/NURSE/PATIENT", "rahul", "Scheduling",
                fields("appointmentId", "number", "patientSsn", "number", "physicianId", "number", "prepNurseId", "number", "start", "datetime", "end", "datetime", "examinationRoom", "string"));
        register("rahul-scheduling-12", "Update Appointment", "PUT", "/api/v1/scheduling/appointments/{id}",
                "Update appointment timing or room.", "ADMIN/DOCTOR", "rahul", "Scheduling",
                fields("id", "number", "appointmentId", "number", "patientSsn", "number", "physicianId", "number", "prepNurseId", "number", "start", "datetime", "end", "datetime", "examinationRoom", "string"));
        register("rahul-scheduling-13", "Delete Appointment", "DELETE", "/api/v1/scheduling/appointments/{id}",
                "Cancel an appointment booking.", "ADMIN/NURSE/PATIENT", "rahul", "Scheduling",
                fields("id", "number"));
    }

    public List<Endpoint> getAllEndpoints() {
        return endpoints;
    }

    public List<Endpoint> getEndpointsByDeveloper(String developerId) {
        return endpoints.stream()
                .filter(e -> e.getDeveloperId().equalsIgnoreCase(developerId))
                .collect(Collectors.toList());
    }

    public Endpoint getEndpointById(String id) {
        return endpoints.stream()
                .filter(e -> e.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    private void register(String id, String name, String method, String path, String description,
                          String access, String developerId, String module, Map<String, String> fields) {
        endpoints.add(Endpoint.builder()
                .id(id)
                .name(name)
                .method(method)
                .path(path)
                .description(description)
                .access(access)
                .developerId(developerId)
                .module(module)
                .fields(fields)
                .build());
    }

    private Map<String, String> fields(String... pair) {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < pair.length; i += 2) {
            map.put(pair[i], pair[i + 1]);
        }
        return map;
    }
}
