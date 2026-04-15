package com.capgemini.hms.prescription.service;

import com.capgemini.hms.appointment.entity.Appointment;
import com.capgemini.hms.appointment.repository.AppointmentRepository;
import com.capgemini.hms.medication.entity.Medication;
import com.capgemini.hms.medication.repository.MedicationRepository;
import com.capgemini.hms.patient.entity.Patient;
import com.capgemini.hms.patient.repository.PatientRepository;
import com.capgemini.hms.physician.entity.Physician;
import com.capgemini.hms.physician.repository.PhysicianRepository;
import com.capgemini.hms.prescription.entity.Prescription;
import com.capgemini.hms.prescription.entity.PrescriptionId;
import com.capgemini.hms.prescription.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PhysicianRepository physicianRepository;
    private final PatientRepository patientRepository;
    private final MedicationRepository medicationRepository;
    private final AppointmentRepository appointmentRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository,
                               PhysicianRepository physicianRepository,
                               PatientRepository patientRepository,
                               MedicationRepository medicationRepository,
                               AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.physicianRepository = physicianRepository;
        this.patientRepository = patientRepository;
        this.medicationRepository = medicationRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public Prescription createPrescription(Prescription prescription, Integer appointmentId) {
        // 1. Validate Core Entities
        Physician doc = physicianRepository.findById(prescription.getPhysician().getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Physician not found"));
        Patient pat = patientRepository.findById(prescription.getPatient().getSsn())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Medication med = medicationRepository.findById(prescription.getMedication().getCode())
                .orElseThrow(() -> new RuntimeException("Medication not found"));

        // 2. Optional Appointment Validation
        if (appointmentId != null) {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
            
            // Consistency check: Appointment must belong to the same patient and doctor
            if (!appointment.getPatient().getSsn().equals(pat.getSsn()) || 
                !appointment.getPhysician().getEmployeeId().equals(doc.getEmployeeId())) {
                throw new RuntimeException("Appointment consistency mismatch: patient or physician does not match");
            }
            prescription.setAppointment(appointment);
        }

        // 3. Set Entity State (Ensure ID matches linked objects)
        prescription.setPhysician(doc);
        prescription.setPatient(pat);
        prescription.setMedication(med);

        return prescriptionRepository.save(prescription);
    }

    public List<Prescription> getPatientPrescriptions(Integer ssn) {
        return prescriptionRepository.findByPatient_Ssn(ssn);
    }
}
