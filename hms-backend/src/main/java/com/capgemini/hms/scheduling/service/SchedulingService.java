package com.capgemini.hms.scheduling.service;

import com.capgemini.hms.common.constants.ErrorMessages;
import com.capgemini.hms.exception.BadRequestException;
import com.capgemini.hms.exception.ResourceNotFoundException;
import com.capgemini.hms.nursing.entity.Nurse;
import com.capgemini.hms.nursing.repository.NurseRepository;
import com.capgemini.hms.patient.entity.Patient;
import com.capgemini.hms.patient.repository.PatientRepository;
import com.capgemini.hms.scheduling.dto.AppointmentDTO;
import com.capgemini.hms.scheduling.dto.PhysicianDTO;
import com.capgemini.hms.scheduling.entity.Appointment;
import com.capgemini.hms.scheduling.entity.Physician;
import com.capgemini.hms.scheduling.repository.AppointmentRepository;
import com.capgemini.hms.scheduling.repository.PhysicianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulingService {

    private final PhysicianRepository physicianRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final NurseRepository nurseRepository;
    private final com.capgemini.hms.scheduling.repository.TrainedInRepository trainedInRepository;
    private final com.capgemini.hms.patient.repository.ProceduresRepository proceduresRepository;

    // --- PHYSICIANS ---

    public List<PhysicianDTO> getAllPhysicians() {
        return physicianRepository.findAll().stream()
                .filter(p -> !Boolean.TRUE.equals(p.getIsDeleted()))
                .map(this::convertToPhysicianDTO)
                .toList();
    }

    public List<PhysicianDTO> searchPhysicians(String query) {
        return physicianRepository.searchActivePhysicians(query).stream()
                .map(this::convertToPhysicianDTO)
                .toList();
    }

    public PhysicianDTO getPhysicianById(Integer id) {
        Physician p = physicianRepository.findByEmployeeIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PHYSICIAN_NOT_FOUND.formatted(id)));
        return convertToPhysicianDTO(p);
    }

    @Transactional
    public PhysicianDTO addPhysician(PhysicianDTO dto) {
        Physician physician = Physician.builder()
                .employeeId(dto.getEmployeeId())
                .name(dto.getName())
                .position(dto.getPosition())
                .ssn(dto.getSsn())
                .build();
        return convertToPhysicianDTO(physicianRepository.save(physician));
    }

    @Transactional
    public PhysicianDTO updatePhysician(Integer id, PhysicianDTO dto) {
        Physician existing = physicianRepository.findByEmployeeIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PHYSICIAN_NOT_FOUND.formatted(id)));
        
        existing.setName(dto.getName());
        existing.setPosition(dto.getPosition());
        existing.setSsn(dto.getSsn());

        return convertToPhysicianDTO(physicianRepository.save(existing));
    }

    @Transactional
    public void deletePhysician(Integer id) {
        Physician p = physicianRepository.findByEmployeeIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PHYSICIAN_NOT_FOUND.formatted(id)));
        p.setIsDeleted(true);
        physicianRepository.save(p);
    }

    public List<com.capgemini.hms.scheduling.dto.TrainedInDTO> getCertificationsByPhysician(Integer id) {
        return trainedInRepository.findByPhysicianEmployeeIdAndIsDeletedFalse(id).stream()
                .map(ti -> com.capgemini.hms.scheduling.dto.TrainedInDTO.builder()
                        .physicianId(ti.getPhysician().getEmployeeId())
                        .physicianName(ti.getPhysician().getName())
                        .procedureCode(ti.getProcedure().getCode())
                        .procedureName(ti.getProcedure().getName())
                        .treatmentDate(ti.getTreatmentdate())
                        .certificationExpires(ti.getCertificationexpires())
                        .build())
                .toList();
    }

    @Transactional
    public com.capgemini.hms.scheduling.dto.TrainedInDTO addCertification(com.capgemini.hms.scheduling.dto.TrainedInDTO dto) {
        Physician physician = physicianRepository.findByEmployeeIdAndIsDeletedFalse(dto.getPhysicianId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PHYSICIAN_NOT_FOUND.formatted(dto.getPhysicianId())));
        
        com.capgemini.hms.patient.entity.Procedures procedure = proceduresRepository.findByCodeAndIsDeletedFalse(dto.getProcedureCode())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PROCEDURE_NOT_FOUND.formatted(dto.getProcedureCode())));

        com.capgemini.hms.scheduling.entity.TrainedIn ti = com.capgemini.hms.scheduling.entity.TrainedIn.builder()
                .id(new com.capgemini.hms.scheduling.entity.TrainedInId(physician.getEmployeeId(), procedure.getCode()))
                .physician(physician)
                .procedure(procedure)
                .treatmentdate(dto.getTreatmentDate())
                .certificationexpires(dto.getCertificationExpires())
                .build();
        
        trainedInRepository.save(ti);
        return dto;
    }

    // --- APPOINTMENTS ---

    @Transactional
    public AppointmentDTO bookAppointment(AppointmentDTO dto) {
        validateAppointment(dto);
        Patient patient = patientRepository.findBySsnAndIsDeletedFalse(dto.getPatientSsn())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PATIENT_NOT_FOUND.formatted(dto.getPatientSsn())));

        Physician physician = physicianRepository.findByEmployeeIdAndIsDeletedFalse(dto.getPhysicianId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PHYSICIAN_NOT_FOUND.formatted(dto.getPhysicianId())));

        Nurse nurse = null;
        if (dto.getPrepNurseId() != null) {
            nurse = nurseRepository.findByEmployeeIdAndIsDeletedFalse(dto.getPrepNurseId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NURSE_NOT_FOUND.formatted(dto.getPrepNurseId())));
        }

        Appointment appointment = Appointment.builder()
                .appointmentId(dto.getAppointmentId())
                .patient(patient)
                .physician(physician)
                .prepNurse(nurse)
                .start(dto.getStart())
                .end(dto.getEnd())
                .examinationRoom(dto.getExaminationRoom())
                .build();

        return convertToAppointmentDTO(appointmentRepository.save(appointment));
    }

    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .filter(a -> !Boolean.TRUE.equals(a.getIsDeleted()))
                .map(this::convertToAppointmentDTO)
                .toList();
    }

    public List<AppointmentDTO> searchAppointments(String query) {
        return appointmentRepository.searchByRoom(query).stream()
                .map(this::convertToAppointmentDTO)
                .toList();
    }

    @Transactional
    public AppointmentDTO updateAppointment(Integer id, AppointmentDTO dto) {
        Appointment existing = appointmentRepository.findByAppointmentIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.APPOINTMENT_NOT_FOUND.formatted(id)));

        validateAppointment(dto);

        existing.setStart(dto.getStart());
        existing.setEnd(dto.getEnd());
        existing.setExaminationRoom(dto.getExaminationRoom());

        if (dto.getPrepNurseId() != null) {
            existing.setPrepNurse(nurseRepository.findByEmployeeIdAndIsDeletedFalse(dto.getPrepNurseId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NURSE_NOT_FOUND.formatted(dto.getPrepNurseId()))));
        }

        return convertToAppointmentDTO(appointmentRepository.save(existing));
    }

    @Transactional
    public void cancelAppointment(Integer id) {
        Appointment appointment = appointmentRepository.findByAppointmentIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.APPOINTMENT_NOT_FOUND.formatted(id)));
        appointment.setIsDeleted(true);
        appointmentRepository.save(appointment);
    }

    private PhysicianDTO convertToPhysicianDTO(Physician p) {
        return PhysicianDTO.builder()
                .employeeId(p.getEmployeeId())
                .name(p.getName())
                .position(p.getPosition())
                .ssn(p.getSsn())
                .build();
    }

    private AppointmentDTO convertToAppointmentDTO(Appointment a) {
        return AppointmentDTO.builder()
                .appointmentId(a.getAppointmentId())
                .patientSsn(a.getPatient().getSsn())
                .physicianId(a.getPhysician().getEmployeeId())
                .prepNurseId(a.getPrepNurse() != null ? a.getPrepNurse().getEmployeeId() : null)
                .start(a.getStart())
                .end(a.getEnd())
                .examinationRoom(a.getExaminationRoom())
                .build();
    }

    private void validateAppointment(AppointmentDTO dto) {
        if (dto.getStart() == null || dto.getEnd() == null) {
            throw new BadRequestException("Appointment start and end times are required.");
        }
        if (!dto.getEnd().isAfter(dto.getStart())) {
            throw new BadRequestException("Appointment end time must be after the start time.");
        }
    }
}
