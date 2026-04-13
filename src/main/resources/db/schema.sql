-- Capgemini HMS Database Schema (Modernized v1)

-- 1. Independent Core Tables
CREATE TABLE physician (
    employeeid INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    position VARCHAR(255) NOT NULL,
    ssn INTEGER NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE nurse (
    employeeid INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    position VARCHAR(255) NOT NULL,
    registered BOOLEAN NOT NULL,
    ssn INTEGER NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE procedures (
    code INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cost DECIMAL(10, 2) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE medication (
    code INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE block (
    blockfloor INTEGER NOT NULL,
    blockcode INTEGER NOT NULL,
    PRIMARY KEY (blockfloor, blockcode)
);

-- 2. Tables with Primary Dependencies
CREATE TABLE department (
    departmentid INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    head INTEGER REFERENCES physician(employeeid),
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE patient (
    ssn INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    phone VARCHAR(20) NOT NULL,
    insuranceid VARCHAR(50) NOT NULL,
    pcp INTEGER REFERENCES physician(employeeid),
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE room (
    roomnumber INTEGER PRIMARY KEY,
    roomtype VARCHAR(255) NOT NULL,
    blockfloor INTEGER NOT NULL,
    blockcode INTEGER NOT NULL,
    unavailable BOOLEAN NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (blockfloor, blockcode) REFERENCES block(blockfloor, blockcode)
);

-- 3. Association and Scheduling Tables
CREATE TABLE affiliated_with (
    physician INTEGER REFERENCES physician(employeeid),
    department INTEGER REFERENCES department(departmentid),
    primaryaffiliation BOOLEAN NOT NULL,
    PRIMARY KEY (physician, department)
);

CREATE TABLE trained_in (
    physician INTEGER REFERENCES physician(employeeid),
    treatment INTEGER REFERENCES procedures(code),
    certificationdate TIMESTAMP NOT NULL,
    certificationexpires TIMESTAMP NOT NULL,
    PRIMARY KEY (physician, treatment)
);

CREATE TABLE appointment (
    appointmentid INTEGER PRIMARY KEY,
    patient INTEGER REFERENCES patient(ssn),
    prepnurse INTEGER REFERENCES nurse(employeeid),
    physician INTEGER REFERENCES physician(employeeid),
    start TIMESTAMP NOT NULL,
    "end" TIMESTAMP NOT NULL,
    examinationroom TEXT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE stay (
    stayid INTEGER PRIMARY KEY,
    patient INTEGER REFERENCES patient(ssn),
    room INTEGER REFERENCES room(roomnumber),
    staystart TIMESTAMP NOT NULL,
    stayend TIMESTAMP NOT NULL,
    notes TEXT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE on_call (
    nurse INTEGER REFERENCES nurse(employeeid),
    blockfloor INTEGER NOT NULL,
    blockcode INTEGER NOT NULL,
    oncallstart TIMESTAMP NOT NULL,
    oncallend TIMESTAMP NOT NULL,
    PRIMARY KEY (nurse, blockfloor, blockcode, oncallstart, oncallend),
    FOREIGN KEY (blockfloor, blockcode) REFERENCES block(blockfloor, blockcode)
);

-- 4. Prescriptions and Treatment Records
CREATE TABLE prescribes (
    physician INTEGER REFERENCES physician(employeeid),
    patient INTEGER REFERENCES patient(ssn),
    medication INTEGER REFERENCES medication(code),
    date TIMESTAMP NOT NULL,
    appointment INTEGER REFERENCES appointment(appointmentid),
    dose TEXT NOT NULL,
    PRIMARY KEY (physician, patient, medication, date)
);

CREATE TABLE undergoes (
    patient INTEGER REFERENCES patient(ssn),
    procedure INTEGER REFERENCES procedures(code),
    stay INTEGER REFERENCES stay(stayid),
    dateundergoes TIMESTAMP NOT NULL,
    physician INTEGER REFERENCES physician(employeeid),
    assistingnurse INTEGER REFERENCES nurse(employeeid),
    notes TEXT,
    PRIMARY KEY (patient, procedure, stay, dateundergoes)
);

-- 5. Authentication and User Management
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(120) NOT NULL
);

CREATE TABLE user_roles (
    user_id INTEGER REFERENCES users(id),
    role_id INTEGER REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
);
