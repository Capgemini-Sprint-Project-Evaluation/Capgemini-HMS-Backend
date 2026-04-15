package com.capgemini.hms.procedure.service;

import com.capgemini.hms.nurse.entity.Nurse;
import com.capgemini.hms.nurse.repository.NurseRepository;
import com.capgemini.hms.patient.entity.Patient;
import com.capgemini.hms.patient.repository.PatientRepository;
import com.capgemini.hms.physician.entity.Physician;
import com.capgemini.hms.physician.repository.PhysicianRepository;
import com.capgemini.hms.procedure.entity.Procedure;
import com.capgemini.hms.procedure.entity.Undergoes;
import com.capgemini.hms.procedure.entity.UndergoesId;
import com.capgemini.hms.procedure.repository.ProcedureRepository;
import com.capgemini.hms.procedure.repository.UndergoesRepository;
import com.capgemini.hms.stay.entity.Stay;
import com.capgemini.hms.stay.repository.StayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalRecordService {

    private final UndergoesRepository undergoesRepository;
    private final ProcedureRepository procedureRepository;
    private final PatientRepository patientRepository;
    private final StayRepository stayRepository;
    private final PhysicianRepository physicianRepository;
    private final NurseRepository nurseRepository;

    public MedicalRecordService(UndergoesRepository undergoesRepository,
                                ProcedureRepository procedureRepository,
                                PatientRepository patientRepository,
                                StayRepository stayRepository,
                                PhysicianRepository physicianRepository,
                                NurseRepository nurseRepository) {
        this.undergoesRepository = undergoesRepository;
        this.procedureRepository = procedureRepository;
        this.patientRepository = patientRepository;
        this.stayRepository = stayRepository;
        this.physicianRepository = physicianRepository;
        this.nurseRepository = nurseRepository;
    }

    @Transactional
    public Undergoes recordProcedure(Integer patientSsn, Integer procedureCode, Integer stayId, 
                                     LocalDateTime date, Integer physicianId, Integer nurseId, String notes) {
        
        Patient patient = patientRepository.findById(patientSsn)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Procedure procedure = procedureRepository.findById(procedureCode)
                .orElseThrow(() -> new RuntimeException("Procedure not found"));
        Stay stay = stayRepository.findById(stayId)
                .orElseThrow(() -> new RuntimeException("Stay record not found"));
        Physician physician = physicianRepository.findById(physicianId)
                .orElseThrow(() -> new RuntimeException("Physician not found"));
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new RuntimeException("Nurse not found"));

        UndergoesId id = new UndergoesId(patientSsn, procedureCode, stayId, date);
        Undergoes undergoes = new Undergoes(id, patient, procedure, stay, physician, nurse, notes);

        return undergoesRepository.save(undergoes);
    }

    public List<Undergoes> getPatientMedicalHistory(Integer patientSsn) {
        return undergoesRepository.findByPatient_Ssn(patientSsn);
    }

    public List<Undergoes> getStayProcedures(Integer stayId) {
        return undergoesRepository.findByStay_StayId(stayId);
    }
}
