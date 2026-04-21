# Security Audit Report: Authentication & RBAC

| Scenario | Expected | Result | Code | Notes |
| :--- | :--- | :--- | :--- | :--- |
| Patient Access Own Record | 200 or 403 | FAIL | 302 |  |
| Privacy Shield (Patient A -> Patient B) | 200 or 403 | FAIL | 302 | Expected 403 |
| Privilege Escalation (Patient -> Admin API) | 200 or 403 | FAIL | 302 | Expected 403 |
| Privilege Escalation (Doctor -> Admin API) | 200 or 403 | FAIL | 302 | Expected 403 |
