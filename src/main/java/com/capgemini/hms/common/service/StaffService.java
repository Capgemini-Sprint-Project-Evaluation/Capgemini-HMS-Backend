package com.capgemini.hms.common.service;

import com.capgemini.hms.nurse.entity.Nurse;
import com.capgemini.hms.nurse.repository.NurseRepository;
import com.capgemini.hms.physician.entity.Physician;
import com.capgemini.hms.physician.repository.PhysicianRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.List;

@Service
public class StaffService {

    private final PhysicianRepository physicianRepository;
    private final NurseRepository nurseRepository;

    public StaffService(PhysicianRepository physicianRepository, NurseRepository nurseRepository) {
        this.physicianRepository = physicianRepository;
        this.nurseRepository = nurseRepository;
    }

    // --- Physician Operations ---

    @Transactional
    public Physician savePhysician(Physician physician) {
        physician.setIsDeleted(false);
        return physicianRepository.save(physician);
    }

    public Page<Physician> getAllPhysicians(Pageable pageable) {
        return physicianRepository.findAllActive(pageable);
    }

    public Optional<Physician> getPhysicianById(Integer id) {
        return physicianRepository.findById(id)
                .filter(p -> !p.getIsDeleted());
    }

    @Transactional
    public Physician updatePhysician(Physician physician) {
        Physician existing = physicianRepository.findById(physician.getEmployeeId())
                .filter(p -> !p.getIsDeleted())
                .orElseThrow(() -> new RuntimeException("Physician not found"));
        
        existing.setName(physician.getName());
        existing.setPosition(physician.getPosition());
        existing.setSsn(physician.getSsn());
        
        return physicianRepository.save(existing);
    }

    @Transactional
    public void deletePhysician(Integer id) {
        Physician p = physicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Physician not found"));
        p.setIsDeleted(true);
        physicianRepository.save(p);
    }

    public Page<Physician> searchPhysicians(String query, Pageable pageable) {
        return physicianRepository.searchActive(query, pageable);
    }

    // --- Nurse Operations ---

    @Transactional
    public Nurse saveNurse(Nurse nurse) {
        nurse.setIsDeleted(false);
        return nurseRepository.save(nurse);
    }

    public Page<Nurse> getAllNurses(Pageable pageable) {
        return nurseRepository.findAllActive(pageable);
    }

    public Optional<Nurse> getNurseById(Integer id) {
        return nurseRepository.findById(id)
                .filter(n -> !n.getIsDeleted());
    }

    @Transactional
    public Nurse updateNurse(Nurse nurse) {
        Nurse existing = nurseRepository.findById(nurse.getEmployeeId())
                .filter(n -> !n.getIsDeleted())
                .orElseThrow(() -> new RuntimeException("Nurse not found"));
        
        existing.setName(nurse.getName());
        existing.setPosition(nurse.getPosition());
        existing.setRegistered(nurse.getRegistered());
        existing.setSsn(nurse.getSsn());
        
        return nurseRepository.save(existing);
    }

    @Transactional
    public void deleteNurse(Integer id) {
        Nurse n = nurseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nurse not found"));
        n.setIsDeleted(true);
        nurseRepository.save(n);
    }

    public List<Nurse> getRegisteredNurses() {
        return nurseRepository.findByRegisteredActive(true);
    }
}
