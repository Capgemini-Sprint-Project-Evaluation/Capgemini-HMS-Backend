package com.capgemini.hms.nursing.repository;

import com.capgemini.hms.nursing.entity.OnCall;
import com.capgemini.hms.nursing.entity.OnCallId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnCallRepository extends JpaRepository<OnCall, OnCallId> {
}
