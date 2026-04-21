package com.capgemini.hms.patient.service;

import com.capgemini.hms.common.constants.ErrorMessages;
import com.capgemini.hms.patient.entity.Patient;
import com.capgemini.hms.patient.entity.Procedures;
import com.capgemini.hms.patient.entity.Stay;
import com.capgemini.hms.patient.entity.Undergoes;
import com.capgemini.hms.patient.entity.UndergoesId;
import com.capgemini.hms.exception.BadRequestException;
import com.capgemini.hms.exception.ResourceNotFoundException;
import com.capgemini.hms.patient.dto.PatientDTO;
import com.capgemini.hms.patient.dto.ProcedureDTO;
import com.capgemini.hms.patient.dto.StayDTO;
import com.capgemini.hms.patient.dto.UndergoesDTO;
import com.capgemini.hms.patient.repository.PatientRepository;
import com.capgemini.hms.scheduling.entity.Physician;
import com.capgemini.hms.scheduling.repository.PhysicianRepository;
import com.capgemini.hms.infrastructure.repository.RoomRepository;
import com.capgemini.hms.infrastructure.entity.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PhysicianRepository physicianRepository;
    private final com.capgemini.hms.patient.repository.ProceduresRepository proceduresRepository;
    private final com.capgemini.hms.patient.repository.UndergoesRepository undergoesRepository;
    private final com.capgemini.hms.patient.repository.StayRepository stayRepository;
    private final RoomRepository roomRepository;

    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .filter(p -> !Boolean.TRUE.equals(p.getIsDeleted()))
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public StayDTO recordStay(StayDTO dto) {
        Patient patient = patientRepository.findBySsnAndIsDeletedFalse(dto.getPatientSsn())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PATIENT_NOT_FOUND.formatted(dto.getPatientSsn())));

        Room room = roomRepository.findByRoomNumberAndIsDeletedFalse(dto.getRoomNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with number: " + dto.getRoomNumber()));

        Integer nextId = stayRepository.findMaxStayId() + 1;

        Stay stay = Stay.builder()
                .stayId(nextId)
                .patient(patient)
                .room(room)
                .stayStart(dto.getStartTime() != null ? dto.getStartTime() : java.time.LocalDateTime.now())
                .stayEnd(dto.getEndTime())
                .isDeleted(false)
                .build();

        Stay saved = stayRepository.save(stay);
        return convertToStayDTO(saved);
    }

    public List<UndergoesDTO> getUndergoesHistory(Integer ssn) {
        return undergoesRepository.findByPatientSsnAndIsDeletedFalse(ssn).stream()
                .map(u -> UndergoesDTO.builder()
                        .patientSsn(u.getPatient() != null ? u.getPatient().getSsn() : null)
                        .procedureCode(u.getProcedure() != null ? u.getProcedure().getCode() : null)
                        .stayId(u.getStay() != null ? u.getStay().getStayId() : null)
                        .physicianId(u.getPhysician() != null ? u.getPhysician().getEmployeeId() : null)
                        .date(u.getId() != null ? u.getId().getDateundergoes() : null)
                        .build())
                .toList();
    }

    @Transactional
    public UndergoesDTO recordProcedure(UndergoesDTO dto) {
        if (dto.getDate() == null) {
            throw new BadRequestException("Procedure date is required.");
        }
        Patient patient = patientRepository.findBySsnAndIsDeletedFalse(dto.getPatientSsn())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PATIENT_NOT_FOUND.formatted(dto.getPatientSsn())));
        
        Procedures procedure = proceduresRepository.findByCodeAndIsDeletedFalse(dto.getProcedureCode())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PROCEDURE_NOT_FOUND.formatted(dto.getProcedureCode())));

        Stay stay = stayRepository.findByStayIdAndIsDeletedFalse(dto.getStayId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.STAY_NOT_FOUND.formatted(dto.getStayId())));

        if (!stay.getPatient().getSsn().equals(patient.getSsn())) {
            throw new BadRequestException("The provided stay does not belong to the given patient.");
        }

        Physician physician = physicianRepository.findByEmployeeIdAndIsDeletedFalse(dto.getPhysicianId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PHYSICIAN_NOT_FOUND.formatted(dto.getPhysicianId())));

        UndergoesId recordId = new UndergoesId(patient.getSsn(), procedure.getCode(), stay.getStayId(), dto.getDate());
        Undergoes record = Undergoes.builder()
                .id(recordId)
                .patient(patient)
                .procedure(procedure)
                .stay(stay)
                .physician(physician)
                .build();

        Undergoes saved = undergoesRepository.save(record);
        return UndergoesDTO.builder()
                .patientSsn(saved.getPatient().getSsn())
                .procedureCode(saved.getProcedure().getCode())
                .stayId(saved.getStay().getStayId())
                .physicianId(saved.getPhysician().getEmployeeId())
                .date(saved.getId().getDateundergoes())
                .build();
    }

    public List<ProcedureDTO> getAllProcedures() {
        return proceduresRepository.findAll().stream()
                .filter(p -> !Boolean.TRUE.equals(p.getIsDeleted()))
                .map(p -> ProcedureDTO.builder()
                        .code(p.getCode())
                        .name(p.getName())
                        .cost(p.getCost())
                        .build())
                .toList();
    }

    public List<ProcedureDTO> searchProcedures(String query) {
        return proceduresRepository.findAll().stream()
                .filter(p -> !Boolean.TRUE.equals(p.getIsDeleted()) && p.getName().toLowerCase().contains(query.toLowerCase()))
                .map(p -> ProcedureDTO.builder()
                        .code(p.getCode())
                        .name(p.getName())
                        .cost(p.getCost())
                        .build())
                .toList();
    }

    public List<ProcedureDTO> getProceduresByPatient(Integer ssn) {
        return undergoesRepository.findByPatientSsnAndIsDeletedFalse(ssn).stream()
                .map(u -> ProcedureDTO.builder()
                        .code(u.getProcedure().getCode())
                        .name(u.getProcedure().getName())
                        .cost(u.getProcedure().getCost())
                        .build())
                .toList();
    }

    @Transactional
    public ProcedureDTO addProcedure(ProcedureDTO dto) {
        Procedures proc = Procedures.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .cost(dto.getCost())
                .build();
        proc = proceduresRepository.save(proc);
        return ProcedureDTO.builder()
                .code(proc.getCode())
                .name(proc.getName())
                .cost(proc.getCost())
                .build();
    }

    public List<PatientDTO> searchPatients(String query) {
        return patientRepository.searchActivePatients(query).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public PatientDTO getPatientBySsn(Integer ssn) {
        Patient patient = patientRepository.findBySsnAndIsDeletedFalse(ssn)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PATIENT_NOT_FOUND.formatted(ssn)));
        return convertToDTO(patient);
    }

    @Transactional
    public PatientDTO savePatient(PatientDTO dto) {
        Physician pcp = findPhysician(dto.getPcpId());

        Patient patient = Patient.builder()
                .ssn(dto.getSsn())
                .name(dto.getName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .insuranceId(dto.getInsuranceId())
                .pcp(pcp)
                .build();

        return convertToDTO(patientRepository.save(patient));
    }

    @Transactional
    public PatientDTO updatePatient(Integer ssn, PatientDTO dto) {
        Patient existing = patientRepository.findBySsnAndIsDeletedFalse(ssn)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PATIENT_NOT_FOUND.formatted(ssn)));
        
        existing.setName(dto.getName());
        existing.setAddress(dto.getAddress());
        existing.setPhone(dto.getPhone());
        existing.setInsuranceId(dto.getInsuranceId());
        existing.setPcp(findPhysician(dto.getPcpId()));

        return convertToDTO(patientRepository.save(existing));
    }

    public List<StayDTO> getStayHistory(Integer ssn) {
        // We verify patient exists first
        getPatientBySsn(ssn); 
        return stayRepository.findAll().stream()
                .filter(s -> s.getPatient() != null && s.getPatient().getSsn().equals(ssn))
                .map(this::convertToStayDTO)
                .toList();
    }

    private Physician findPhysician(Integer id) {
        if (id == null) {
            return null;
        }
        return physicianRepository.findByEmployeeIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PHYSICIAN_NOT_FOUND.formatted(id)));
    }

    @Transactional
    public void deletePatient(Integer ssn) {
        Patient patient = patientRepository.findBySsnAndIsDeletedFalse(ssn)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PATIENT_NOT_FOUND.formatted(ssn)));
        patient.setIsDeleted(true);
        patientRepository.save(patient);
    }

    public List<StayDTO> getActiveStays() {
        return stayRepository.findByStayEndIsNullAndIsDeletedFalse().stream()
                .map(this::convertToStayDTO)
                .toList();
    }

    @Transactional
    public StayDTO updateStay(Integer id, StayDTO dto) {
        Stay existing = stayRepository.findByStayIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.STAY_NOT_FOUND.formatted(id)));

        existing.setStayStart(dto.getStartTime());
        existing.setStayEnd(dto.getEndTime());
        
        // Potential room update logic could go here if Stay linked to RoomEntity directly
        // Currently it's linked via Room number in the entity if mapping exists
        
        return convertToStayDTO(stayRepository.save(existing));
    }

    private StayDTO convertToStayDTO(Stay stay) {
        return StayDTO.builder()
                .stayId(stay.getStayId())
                .patientSsn(stay.getPatient() != null ? stay.getPatient().getSsn() : null)
                .roomNumber(stay.getRoom() != null ? stay.getRoom().getRoomNumber() : null)
                .startTime(stay.getStayStart())
                .endTime(stay.getStayEnd())
                .build();
    }

    private PatientDTO convertToDTO(Patient patient) {
        return PatientDTO.builder()
                .ssn(patient.getSsn())
                .name(patient.getName())
                .address(patient.getAddress())
                .phone(patient.getPhone())
                .insuranceId(patient.getInsuranceId())
                .pcpId(patient.getPcp() != null ? patient.getPcp().getEmployeeId() : null)
                .build();
    }
}
