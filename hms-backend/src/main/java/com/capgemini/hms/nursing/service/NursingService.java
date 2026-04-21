package com.capgemini.hms.nursing.service;

import com.capgemini.hms.common.constants.ErrorMessages;
import com.capgemini.hms.exception.ResourceNotFoundException;
import com.capgemini.hms.nursing.dto.NurseDTO;
import com.capgemini.hms.nursing.entity.Nurse;
import com.capgemini.hms.nursing.repository.NurseRepository;
import com.capgemini.hms.nursing.repository.OnCallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NursingService {

    private final NurseRepository nurseRepository;
    private final OnCallRepository onCallRepository;

    public List<NurseDTO> getAllNurses() {
        return nurseRepository.findAll().stream()
                .filter(n -> !Boolean.TRUE.equals(n.getIsDeleted()))
                .map(this::convertToDTO)
                .toList();
    }

    public List<NurseDTO> searchNurses(String query) {
        return nurseRepository.searchActiveNurses(query).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public NurseDTO getNurseById(Integer id) {
        Nurse nurse = nurseRepository.findByEmployeeIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NURSE_NOT_FOUND.formatted(id)));
        return convertToDTO(nurse);
    }

    @Transactional
    public NurseDTO registerNurse(NurseDTO dto) {
        Nurse nurse = Nurse.builder()
                .employeeId(dto.getEmployeeId())
                .name(dto.getName())
                .position(dto.getPosition())
                .registered(dto.getRegistered())
                .ssn(dto.getSsn())
                .build();
        
        return convertToDTO(nurseRepository.save(nurse));
    }

    @Transactional
    public NurseDTO updateNurse(Integer id, NurseDTO dto) {
        Nurse existing = nurseRepository.findByEmployeeIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NURSE_NOT_FOUND.formatted(id)));
        
        existing.setName(dto.getName());
        existing.setPosition(dto.getPosition());
        existing.setRegistered(dto.getRegistered());
        existing.setSsn(dto.getSsn());

        return convertToDTO(nurseRepository.save(existing));
    }

    @Transactional
    public void deleteNurse(Integer id) {
        Nurse nurse = nurseRepository.findByEmployeeIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NURSE_NOT_FOUND.formatted(id)));
        nurse.setIsDeleted(true);
        nurseRepository.save(nurse);
    }

    public List<com.capgemini.hms.nursing.dto.OnCallDTO> getOnCallRotation() {
        return onCallRepository.findAll().stream()
                .map(oc -> com.capgemini.hms.nursing.dto.OnCallDTO.builder()
                        .nurseId(oc.getNurse().getEmployeeId())
                        .nurseName(oc.getNurse().getName())
                        .blockFloor(oc.getId().getBlockFloor())
                        .blockCode(oc.getId().getBlockCode())
                        .onCallStart(oc.getId().getOnCallStart())
                        .onCallEnd(oc.getId().getOnCallEnd())
                        .build())
                .toList();
    }

    private NurseDTO convertToDTO(Nurse nurse) {
        return NurseDTO.builder()
                .employeeId(nurse.getEmployeeId())
                .name(nurse.getName())
                .position(nurse.getPosition())
                .registered(nurse.getRegistered())
                .ssn(nurse.getSsn())
                .build();
    }
}
