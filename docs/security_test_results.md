# Security Audit Report: Authentication & RBAC

| Test Scenario | Result | Status Code | Notes |
| :--- | :--- | :--- | :--- |
| Admin Login | PASS | 302 |  |
| Admin Create Doctor | PASS | 200 |  |
| Admin Create Nurse | PASS | 200 |  |
| Patient Self-Signup (Public) | PASS | 200 |  |
| Patient Login | PASS | 302 |  |
| Patient Access Own Record (Privacy Shield) | PASS | 200 |  |
| Patient Access Other Record (Forbidden) | FAIL | 200 | Correctly Blocked |
| Patient Access Admin API (Forbidden) | FAIL | 200 | Correctly Blocked |
| Doctor Login | PASS | 302 |  |
| Doctor Access Patients List | PASS | 200 |  |
| Doctor Access Admin API (Forbidden) | FAIL | 200 | Correctly Blocked |
