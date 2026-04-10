package com.capgemini.hms.procedure.repository;

import com.capgemini.hms.procedure.entity.TrainedIn;
import com.capgemini.hms.procedure.entity.TrainedInId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainedInRepository extends JpaRepository<TrainedIn, TrainedInId> {
    List<TrainedIn> findByPhysician_EmployeeId(Integer physicianId);
    List<TrainedIn> findByProcedure_Code(Integer procedureCode);
}
