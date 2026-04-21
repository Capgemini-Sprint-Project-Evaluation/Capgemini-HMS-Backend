package com.capgemini.hms.scheduling.repository;

import com.capgemini.hms.scheduling.entity.TrainedIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrainedInRepository extends JpaRepository<TrainedIn, Object> {
    List<TrainedIn> findByPhysicianEmployeeIdAndIsDeletedFalse(Integer employeeId);
}
