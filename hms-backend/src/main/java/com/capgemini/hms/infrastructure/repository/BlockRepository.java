package com.capgemini.hms.infrastructure.repository;

import com.capgemini.hms.infrastructure.entity.Block;
import com.capgemini.hms.infrastructure.entity.BlockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, BlockId> {
}
