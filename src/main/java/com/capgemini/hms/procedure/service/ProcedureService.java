package com.capgemini.hms.procedure.service;

import com.capgemini.hms.procedure.entity.Procedure;
import com.capgemini.hms.procedure.repository.ProcedureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
public class ProcedureService {

    private final ProcedureRepository procedureRepository;

    public ProcedureService(ProcedureRepository procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @Transactional
    public Procedure saveProcedure(Procedure procedure) {
        procedure.setIsDeleted(false);
        return procedureRepository.save(procedure);
    }

    public Page<Procedure> getAllProcedures(Pageable pageable) {
        return procedureRepository.findAllActive(pageable);
    }

    public Optional<Procedure> getProcedureByCode(Integer code) {
        return procedureRepository.findById(code)
                .filter(p -> !p.getIsDeleted());
    }

    @Transactional
    public Procedure updateProcedure(Procedure procedure) {
        Procedure existing = procedureRepository.findById(procedure.getCode())
                .filter(p -> !p.getIsDeleted())
                .orElseThrow(() -> new RuntimeException("Procedure not found"));
        
        existing.setName(procedure.getName());
        existing.setCost(procedure.getCost());
        
        return procedureRepository.save(existing);
    }

    public Page<Procedure> searchProcedures(String query, Pageable pageable) {
        return procedureRepository.searchActive(query, pageable);
    }

    @Transactional
    public void deleteProcedure(Integer code) {
        Procedure proc = procedureRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Procedure not found"));
        proc.setIsDeleted(true);
        procedureRepository.save(proc);
    }
}
