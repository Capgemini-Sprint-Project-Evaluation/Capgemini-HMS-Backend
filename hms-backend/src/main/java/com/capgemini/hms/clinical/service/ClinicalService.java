package com.capgemini.hms.clinical.service;

import com.capgemini.hms.clinical.dto.MedicationDTO;
import com.capgemini.hms.clinical.dto.PrescriptionDTO;
import com.capgemini.hms.clinical.entity.Medication;
import com.capgemini.hms.clinical.entity.Prescription;
import com.capgemini.hms.clinical.entity.PrescriptionId;
import com.capgemini.hms.common.constants.ErrorMessages;
import com.capgemini.hms.exception.BadRequestException;
import com.capgemini.hms.exception.ResourceNotFoundException;
import com.capgemini.hms.clinical.repository.MedicationRepository;
import com.capgemini.hms.clinical.repository.PrescriptionRepository;
import com.capgemini.hms.patient.entity.Patient;
import com.capgemini.hms.patient.repository.PatientRepository;
import com.capgemini.hms.scheduling.entity.Physician;
import com.capgemini.hms.scheduling.repository.PhysicianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicalService {
    private final MedicationRepository medicationRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final PhysicianRepository physicianRepository;
    private final PatientRepository patientRepository;

    public List<MedicationDTO> getAllMedications() {
        return medicationRepository.findAll().stream()
                .filter(m -> !Boolean.TRUE.equals(m.getIsDeleted()))
                .map(this::convertToMedicationDTO)
                .toList();
    }

    public List<MedicationDTO> searchMedications(String query) {
        return medicationRepository.searchActiveMedications(query).stream()
                .map(this::convertToMedicationDTO)
                .toList();
    }

    public MedicationDTO getMedicationByCode(Integer code) {
        Medication m = medicationRepository.findByCodeAndIsDeletedFalse(code)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.MEDICATION_NOT_FOUND.formatted(code)));
        return convertToMedicationDTO(m);
    }

    @Transactional
    public MedicationDTO addMedication(MedicationDTO dto) {
        Medication medication = Medication.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .brand(dto.getBrand())
                .description(dto.getDescription())
                .build();
        return convertToMedicationDTO(medicationRepository.save(medication));
    }

    @Transactional
    public MedicationDTO updateMedication(Integer code, MedicationDTO dto) {
        Medication existing = medicationRepository.findByCodeAndIsDeletedFalse(code)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.MEDICATION_NOT_FOUND.formatted(code)));
        
        existing.setName(dto.getName());
        existing.setBrand(dto.getBrand());
        existing.setDescription(dto.getDescription());

        return convertToMedicationDTO(medicationRepository.save(existing));
    }

    @Transactional
    public void deleteMedication(Integer code) {
        Medication m = medicationRepository.findByCodeAndIsDeletedFalse(code)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.MEDICATION_NOT_FOUND.formatted(code)));
        m.setIsDeleted(true);
        medicationRepository.save(m);
    }

    @Transactional
    public PrescriptionDTO prescribe(PrescriptionDTO dto) {
        validatePrescription(dto);

        Physician physician = physicianRepository.findByEmployeeIdAndIsDeletedFalse(dto.getPhysicianId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PHYSICIAN_NOT_FOUND.formatted(dto.getPhysicianId())));

        Patient patient = patientRepository.findBySsnAndIsDeletedFalse(dto.getPatientSsn())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PATIENT_NOT_FOUND.formatted(dto.getPatientSsn())));

        Medication techMed = medicationRepository.findByCodeAndIsDeletedFalse(dto.getMedicationCode())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.MEDICATION_NOT_FOUND.formatted(dto.getMedicationCode())));

        PrescriptionId pId = new PrescriptionId(physician.getEmployeeId(), patient.getSsn(), techMed.getCode(), dto.getDate());

        Prescription prescription = Prescription.builder()
                .id(pId)
                .physician(physician)
                .patient(patient)
                .medication(techMed)
                .dose(dto.getDose())
                .appointmentId(dto.getAppointmentId())
                .build();

        return convertToPrescriptionDTO(prescriptionRepository.save(prescription));
    }

    public List<PrescriptionDTO> getPrescriptionsByPatient(Integer ssn) {
        return prescriptionRepository.findByIdPatient(ssn).stream()
                .map(this::convertToPrescriptionDTO)
                .toList();
    }

    private MedicationDTO convertToMedicationDTO(Medication m) {
        return MedicationDTO.builder()
                .code(m.getCode())
                .name(m.getName())
                .brand(m.getBrand())
                .description(m.getDescription())
                .build();
    }

    private PrescriptionDTO convertToPrescriptionDTO(Prescription p) {
        return PrescriptionDTO.builder()
                .physicianId(p.getPhysician().getEmployeeId())
                .patientSsn(p.getPatient().getSsn())
                .medicationCode(p.getMedication().getCode())
                .date(p.getId().getDate())
                .dose(p.getDose())
                .appointmentId(p.getAppointmentId())
                .build();
    }

    private void validatePrescription(PrescriptionDTO dto) {
        if (dto.getDate() == null) {
            throw new BadRequestException("Prescription date is required.");
        }
    }
}
