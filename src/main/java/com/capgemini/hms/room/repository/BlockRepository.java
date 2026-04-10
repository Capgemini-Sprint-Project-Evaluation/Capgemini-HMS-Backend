package com.capgemini.hms.room.repository;

import com.capgemini.hms.room.entity.Block;
import com.capgemini.hms.room.entity.BlockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, BlockId> {
}
