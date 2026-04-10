package com.capgemini.hms.procedure.service;

import com.capgemini.hms.procedure.dto.CertificationRequest;
import com.capgemini.hms.physician.entity.Physician;
import com.capgemini.hms.physician.repository.PhysicianRepository;
import com.capgemini.hms.procedure.entity.Procedure;
import com.capgemini.hms.procedure.entity.TrainedIn;
import com.capgemini.hms.procedure.entity.TrainedInId;
import com.capgemini.hms.procedure.repository.ProcedureRepository;
import com.capgemini.hms.procedure.repository.TrainedInRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CertificationService {

    private final TrainedInRepository trainedInRepository;
    private final PhysicianRepository physicianRepository;
    private final ProcedureRepository procedureRepository;

    public CertificationService(TrainedInRepository trainedInRepository,
                                PhysicianRepository physicianRepository,
                                ProcedureRepository procedureRepository) {
        this.trainedInRepository = trainedInRepository;
        this.physicianRepository = physicianRepository;
        this.procedureRepository = procedureRepository;
    }

    @Transactional
    public TrainedIn certifyPhysician(CertificationRequest request) {
        // 1. Validate Date Logic
        if (request.getCertificationExpires().isBefore(request.getCertificationDate())) {
            throw new RuntimeException("Certification expiry date cannot be before the certification date");
        }

        // 2. Validate Entities
        Physician doc = physicianRepository.findById(request.getPhysicianId())
                .orElseThrow(() -> new RuntimeException("Physician not found"));
        Procedure proc = procedureRepository.findById(request.getProcedureCode())
                .orElseThrow(() -> new RuntimeException("Procedure not found"));

        // 3. Create and Save
        TrainedInId id = new TrainedInId(request.getPhysicianId(), request.getProcedureCode());
        TrainedIn certification = new TrainedIn(
                id, 
                doc, 
                proc, 
                request.getCertificationDate(), 
                request.getCertificationExpires()
        );

        return trainedInRepository.save(certification);
    }

    public List<TrainedIn> getPhysicianCertifications(Integer physicianId) {
        return trainedInRepository.findByPhysician_EmployeeId(physicianId);
    }
}
