-- Capgemini HMS Dummy Data (Modernized v1)

-- 1. Authentication Roles
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_DOCTOR');
INSERT INTO roles (name) VALUES ('ROLE_NURSE');
INSERT INTO roles (name) VALUES ('ROLE_PATIENT');

-- 2. Initial Users
-- Admin (Password: admin123)
-- Patient User (Password: password123)
INSERT INTO users (username, email, password, patient_ssn) VALUES 
('admin', 'admin@hms.com', '$2a$10$8.UnVuG9HHgffUDAlk8qn.6nQH22LryWjzEXPp/REuIu7i5pS.66m', NULL),
('john_patient', 'john@gmail.com', '$2a$10$8.UnVuG9HHgffUDAlk8qn.6nQH22LryWjzEXPp/REuIu7i5pS.66m', 1001);

INSERT INTO user_roles (user_id, role_id) VALUES 
(1, 2), -- admin -> ROLE_ADMIN
(2, 5); -- john_patient -> ROLE_PATIENT

-- 3. Physicians
INSERT INTO physician (employeeid, name, position, ssn, is_deleted) VALUES 
(101, 'Dr. Alice Smith', 'Chief Surgeon', 111222, false),
(102, 'Dr. Bob Johnson', 'Senior Physician', 333444, false),
(103, 'Dr. Claire Williams', 'Cardiologist', 555666, false);

-- 4. Nurses
INSERT INTO nurse (employeeid, name, position, registered, ssn, is_deleted) VALUES 
(201, 'Nurse Nancy', 'Head Nurse', true, 777888, false),
(202, 'Nurse Ned', 'Staff Nurse', true, 999000, false);

-- 5. Blocks
INSERT INTO block (blockfloor, blockcode) VALUES 
(1, 1), (1, 2), (2, 1);

-- 6. Departments
INSERT INTO department (departmentid, name, head, is_deleted) VALUES 
(1, 'Surgery', 101, false),
(2, 'Cardiology', 103, false);

-- 7. Rooms
INSERT INTO room (roomnumber, roomtype, blockfloor, blockcode, unavailable, is_deleted) VALUES 
(101, 'Single', 1, 1, false, false),
(102, 'Double', 1, 1, false, false),
(201, 'ICU', 2, 1, false, false);

-- 8. Patients
INSERT INTO patient (ssn, name, address, phone, insuranceid, pcp, is_deleted) VALUES 
(1001, 'John Doe', '123 Health St, City', '555-0101', 'INS-001', 102, false),
(1002, 'Jane Smith', '456 Wellness Rd, City', '555-0102', 'INS-002', 103, false);

-- 9. Procedures
INSERT INTO procedures (code, name, cost, is_deleted) VALUES 
(501, 'Appendectomy', 1500.00, false),
(502, 'Echocardiogram', 800.00, false);

-- 10. Medications
INSERT INTO medication (code, name, brand, description, is_deleted) VALUES 
(601, 'Amoxicillin', 'Amoxil', 'Antibiotic for bacterial infections', false),
(602, 'Lisinopril', 'Zestril', 'Medication for high blood pressure', false);

-- 11. Sample Stays & Appointments
INSERT INTO stay (stayid, patient, room, staystart, stayend, notes, is_deleted) VALUES 
(1, 1001, 101, '2026-04-01 10:00:00', '2026-04-05 14:00:00', 'Initial stay for monitoring', false);

INSERT INTO appointment (appointmentid, patient, prepnurse, physician, start_time, end_time, examinationroom, is_deleted) VALUES 
(1, 1001, 201, 102, '2026-04-10 09:00:00', '2026-04-10 10:00:00', 'Room A-1', false);
