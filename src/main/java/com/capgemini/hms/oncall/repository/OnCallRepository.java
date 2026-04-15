package com.capgemini.hms.oncall.repository;

import com.capgemini.hms.oncall.entity.OnCall;
import com.capgemini.hms.oncall.entity.OnCallId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OnCallRepository extends JpaRepository<OnCall, OnCallId> {

    @Query("SELECT o FROM OnCall o WHERE o.nurse.employeeId = :nurseId " +
           "AND o.id.onCallStart < :endTime AND o.id.onCallEnd > :startTime")
    List<OnCall> findOverlappingShifts(
            @Param("nurseId") Integer nurseId, 
            @Param("startTime") LocalDateTime startTime, 
            @Param("endTime") LocalDateTime endTime);

    List<OnCall> findByNurse_EmployeeId(Integer nurseId);
    List<OnCall> findById_BlockFloorAndId_BlockCode(Integer floor, Integer code);
}
