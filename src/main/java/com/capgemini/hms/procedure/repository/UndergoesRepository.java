package com.capgemini.hms.procedure.repository;

import com.capgemini.hms.procedure.entity.Undergoes;
import com.capgemini.hms.procedure.entity.UndergoesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UndergoesRepository extends JpaRepository<Undergoes, UndergoesId> {
    List<Undergoes> findByPatient_Ssn(Integer ssn);
    List<Undergoes> findByStay_StayId(Integer stayId);

    @Query("SELECT SUM(u.procedure.cost) FROM Undergoes u")
    Double calculateTotalRevenue();
}
