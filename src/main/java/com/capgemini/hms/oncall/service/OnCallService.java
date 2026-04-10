package com.capgemini.hms.oncall.service;

import com.capgemini.hms.nurse.entity.Nurse;
import com.capgemini.hms.nurse.repository.NurseRepository;
import com.capgemini.hms.oncall.entity.OnCall;
import com.capgemini.hms.oncall.entity.OnCallId;
import com.capgemini.hms.oncall.repository.OnCallRepository;
import com.capgemini.hms.room.entity.Block;
import com.capgemini.hms.room.entity.BlockId;
import com.capgemini.hms.room.repository.BlockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OnCallService {

    private final OnCallRepository onCallRepository;
    private final NurseRepository nurseRepository;
    private final BlockRepository blockRepository;

    public OnCallService(OnCallRepository onCallRepository, 
                         NurseRepository nurseRepository, 
                         BlockRepository blockRepository) {
        this.onCallRepository = onCallRepository;
        this.nurseRepository = nurseRepository;
        this.blockRepository = blockRepository;
    }

    @Transactional
    public OnCall assignShift(Integer nurseId, Integer floor, Integer code, LocalDateTime start, LocalDateTime end) {
        // 1. Validate Time
        if (end.isBefore(start)) {
            throw new RuntimeException("Shift end time cannot be before start time");
        }

        // 2. Validate Nurse
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new RuntimeException("Nurse not found"));

        // 3. Validate Block
        BlockId blockId = new BlockId(floor, code);
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new RuntimeException("Hospital Block (" + floor + "-" + code + ") not found"));

        // 4. Overlap Detection
        List<OnCall> overlaps = onCallRepository.findOverlappingShifts(nurseId, start, end);
        if (!overlaps.isEmpty()) {
            throw new RuntimeException("Nurse is already assigned to another shift during this time period");
        }

        // 5. Create and Save
        OnCallId id = new OnCallId(nurseId, floor, code, start, end);
        OnCall shift = new OnCall(id, nurse, block);
        
        return onCallRepository.save(shift);
    }

    public List<OnCall> getNurseShifts(Integer nurseId) {
        return onCallRepository.findByNurse_EmployeeId(nurseId);
    }

    public List<OnCall> getBlockCoverage(Integer floor, Integer code) {
        return onCallRepository.findById_BlockFloorAndId_BlockCode(floor, code);
    }

    @Transactional
    public void deleteShift(Integer nurseId, Integer floor, Integer code, LocalDateTime start, LocalDateTime end) {
        OnCallId id = new OnCallId(nurseId, floor, code, start, end);
        onCallRepository.deleteById(id);
    }
}
