package com.capgemini.hms.infrastructure.repository;

import com.capgemini.hms.infrastructure.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Optional<Room> findByRoomNumberAndIsDeletedFalse(Integer roomNumber);
}
