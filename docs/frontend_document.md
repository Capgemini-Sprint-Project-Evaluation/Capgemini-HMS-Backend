# HMS Frontend Implementation Guide (Modernized v1)

This document is the **Full Blueprint** for building the HMS Frontend. It combines technical API contracts with real-world **User Flow Scenarios** to guide the UI development.

---

## 1. PROJECT ARCHITECTURE
*   **Backend**: Spring Boot 3 + JWT Security + MySQL.
*   **Frontend**: Thymeleaf templates (Server-side shell) + JavaScript (Client-side API calls).
*   **Aesthetics**: Professional Medical Theme (Clean whitespace, `#3498db` primary colors, Bootstrap 5.3).

---

## 2. AUTHENTICATION & INITIALIZATION FLOW
Every user interaction starts here.

### Scenario: User Login & Session Management
1.  **Form Submission**: User enters email/password on `/auth/login`.
2.  **API Call**: `POST /api/v1/auth/signin`.
3.  **Token Storage**: On success, JS stores `accessToken` and `roles` in `localStorage`.
4.  **Dynamic Routing**: JS checks `roles` array and redirects:
    *   `ROLE_ADMIN` -> `/dashboard/admin`
    *   `ROLE_DOCTOR` -> `/dashboard/doctor`
    *   `ROLE_NURSE` -> `/dashboard/nurse`
    *   `ROLE_PATIENT` -> `/dashboard/patient`
5.  **Persistent Header**: Every subsequent `fetch()` request must attach:
    `Authorization: Bearer <token>`

---

## 3. FLOW SCENARIOS BY ROLE

### A. PATIENT SELF-SERVICE FLOW
*Goal: Empower patients to manage their own care.*

1.  **Registration**: New patient uses `/auth/signup` with role `patient`. They MUST provide their existing `patientSsn` to link their login to their clinical record.
2.  **Dashboard Load**: JS calls `GET /api/v1/patients/me` to display personal contact info.
3.  **History Timeline**: Patient views `GET /api/v1/medical-records/my` to see a vertical timeline of all past procedures and doctor notes.
4.  **Booking**: Patient fills a form (Date, Physician, Room) and hits `POST /api/v1/appointments`.
5.  **Active Care**: Patient checks `GET /api/v1/prescriptions/my` to see current dosage instructions from their doctor.

### B. NURSE CLINICAL FLOW
*Goal: Manage patient admissions and facility logistics.*

1.  **Admission**: Nurse identifies a patient, finds an available room (`GET /api/v1/rooms/available`), and registers a stay: `POST /api/v1/stays`.
2.  **Shift Management**: Nurse views personal duty schedule via `GET /api/v1/shifts/nurse/{id}`.
3.  **Rounding**: Nurse views `GET /api/v1/stays/active` to see a list of all current inpatients and their assigned rooms.
4.  **Discharge**: Nurse updates the stay record with `PUT /api/v1/stays/{id}?notes=...` to record discharge time and release the room.

### C. DOCTOR CLINICAL FLOW
*Goal: Diagnostic and Treatment documentation.*

1.  **Daily Schedule**: Doctor views `GET /api/v1/appointments/physician/{id}` for the day's queue.
2.  **Patient Consultation**: During the appointment, doctor views the patient's full history: `GET /api/v1/medical-records/patient/{ssn}`.
3.  **Prescribing**: Doctor issues a medication order: `POST /api/v1/prescriptions`.
4.  **Recording Procedures**: Doctor records the results of a procedure: `POST /api/v1/medical-records/procedure`.
5.  **Skill Tracking**: Doctor checks their own certifications: `GET /api/v1/certifications/physician/{id}`.

### D. ADMIN MANAGEMENT FLOW
*Goal: System onboarding and data integrity.*

1.  **Staff Onboarding**: Admin creates accounts for new Doctors/Nurses using `POST /api/v1/auth/signup`.
2.  **Facility Setup**: Admin adds new rooms (`POST /api/v1/rooms`) or creates departments (`POST /api/v1/departments`).
3.  **Command & Control**: Admin assigns a Department Head via `PUT /api/v1/departments/{id}/head?physicianId=...`.
4.  **Analytics**: Admin views the executive dashboard `GET /api/v1/dashboard/summary` for hospital-wide occupancy and status.

---

## 4. API CONTRACTS (VERIFIED)

| Feature | Endpoint | Method | Payload Key Fields |
| :--- | :--- | :--- | :--- |
| **Auth** | `/api/v1/auth/signin` | POST | `email`, `password` |
| **Register** | `/api/v1/auth/signup` | POST | `username`, `role[]`, `patientSsn` (if patient) |
| **Appointments** | `/api/v1/appointments` | POST | `patientSsn`, `physicianId`, `start`, `end` |
| **Admissions** | `/api/v1/stays` | POST | `patientSsn`, `roomNumber`, `stayStart` |
| **Prescribe** | `/api/v1/prescriptions` | POST | `patientSsn`, `medicationCode`, `dose` |
| **Analytics** | `/api/v1/dashboard/summary` | GET | N/A (Admin only) |

---

## 5. REUSABLE UI COMPONENTS
*   **Searchable Dropdowns**: For selecting Physicians or Patients by SSN.
*   **Status Badges**: For rooms (Green = Available, Red = Occupied).
*   **Timeline Component**: For displaying medical history and stay history.
*   **Form Validation**: Display field-level errors returned in the `ApiResponse` `details` map.

---

## 6. ERROR HANDLING STRATEGY
*   **401 Unauthenticated**: Redirect to `/login`.
*   **403 Unauthorized**: Show "Access Denied" modal.
*   **400 Validation Error**: Use JavaScript to catch the `details` object and highlight individual form inputs in red with the helper text provided by the backend.
