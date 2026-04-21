# HMS Production API Documentation (v1)

This document provides a comprehensive list of all REST endpoints available in the Hospital Management System (HMS) as of the v1 architectural refactor.

## Global Standards
- **Base Version**: `/api/v1`
- **Response Format**: Standardized `ApiResponse<T>` wrapper.
  ```json
  {
    "status": "success",
    "data": { ... },
    "message": "Operation successful"
  }
  ```
- **Pagination**: List endpoints return a `PagedResponse` structure within the `data` field.
- **Security**: Bearer Token required. Roles: `ADMIN`, `DOCTOR`, `NURSE`, `PATIENT`.

---

## 1. Authentication Module
Manage user sessions and security tokens.

| Method | Endpoint | Description | Roles |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/signup` | Register a new user | Admin (or Public) |
| `POST` | `/api/v1/auth/signin` | Authenticate and get JWT | Public |
| `POST` | `/api/v1/auth/refresh` | Refresh JWT using Refresh Token | Authenticated |

---

## 2. Patient Module (Management)
Clinical record management for hospital staff.

| Method | Endpoint | Description | Roles |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/patients` | Paginated list of all active patients | Admin, Doctor, Nurse |
| `GET` | `/api/v1/patients/{ssn}` | Get specific clinical record | Admin, Doctor, Nurse |
| `POST` | `/api/v1/patients` | Register a new clinical record | Admin, Nurse |
| `PUT` | `/api/v1/patients/{ssn}` | Update clinical information | Admin, Nurse |
| `DELETE`| `/api/v1/patients/{ssn}` | Soft-delete a patient record | Admin |
| `GET` | `/api/v1/patients/search` | Search patients by name | Any Staff |

---

## 3. Staff Module (Physicians & Nurses)
Management of hospital medical professionals.

### Physicians
| Method | Endpoint | Description | Roles |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/physicians` | List all active physicians | Any Staff |
| `GET` | `/api/v1/physicians/{id}` | Get physician profile | Any Staff |
| `POST` | `/api/v1/physicians` | Add new physician record | Admin |
| `PUT` | `/api/v1/physicians/{id}` | Update physician profile | Admin |
| `DELETE`| `/api/v1/physicians/{id}` | Soft-delete a physician | Admin |

### Nurses
| Method | Endpoint | Description | Roles |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/nurses` | List all active nurses | Any Staff |
| `GET` | `/api/v1/nurses/{id}` | Get nurse profile | Any Staff |
| `POST` | `/api/v1/nurses` | Add new nurse record | Admin |
| `PUT` | `/api/v1/nurses/{id}` | Update nurse profile | Admin |
| `DELETE`| `/api/v1/nurses/{id}` | Soft-delete a nurse | Admin |

---

## 4. Infrastructure Module
Rooms, Blocks, and Departments.

| Method | Endpoint | Description | Roles |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/rooms` | List all hospital rooms | Any Staff |
| `POST` | `/api/v1/rooms` | Register a new room | Admin |
| `DELETE`| `/api/v1/rooms/{id}` | Soft-delete a room | Admin |
| `GET` | `/api/v1/departments` | List all departments | Any Staff |
| `POST` | `/api/v1/departments` | Create new department | Admin |
| `PUT` | `/api/v1/departments/{id}/head` | Assign department head | Admin |

---

## 5. Clinical Operations (Admin Scoped)
Admissions and scheduling managed by hospital staff.

| Method | Endpoint | Description | Roles |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/stays` | Admit patient (Check-in) | Admin, Nurse |
| `PUT` | `/api/v1/stays/{id}` | Update stay / Discharge | Admin, Nurse |
| `GET` | `/api/v1/stays/active` | Current inpatients | Any Staff |
| `POST` | `/api/v1/appointments` | Book new appointment | Admin, Nurse |
| `DELETE`| `/api/v1/appointments/{id}` | Cancel appointment | Admin, Nurse |

---

## 6. Patient Self-Service Portal (Self-Service)
Endpoints specifically for users with the `PATIENT` role. These automatically scope data to the authenticated user's SSN.

| Method | Endpoint | Description | Roles |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/patients/me` | Get my clinical profile | Patient |
| `PUT` | `/api/v1/patients/me` | Update my contact info | Patient |
| `GET` | `/api/v1/appointments/my` | My upcoming appointments | Patient |
| `GET` | `/api/v1/prescriptions/my` | My active prescriptions | Patient |
| `GET` | `/api/v1/stays/my` | My hospital stay history | Patient |
| `GET` | `/api/v1/medical-records/my`| My complete medical history | Patient |

---

## 7. Medical Records & Pharmacy (Staff Scoped)
Prescriptions and procedural history recording.

| Method | Endpoint | Description | Roles |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/prescriptions` | Create new prescription | Doctor |
| `GET` | `/api/v1/prescriptions/patient/{ssn}`| History for specific patient | Staff |
| `POST` | `/api/v1/medical-records/procedure` | Record procedure result | Doctor |
| `GET` | `/api/v1/medical-records/patient/{ssn}`| Detailed history by SSN | Staff |
