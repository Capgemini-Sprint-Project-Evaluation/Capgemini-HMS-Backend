package com.capgemini.hms.room.service;

import com.capgemini.hms.room.entity.Block;
import com.capgemini.hms.room.entity.BlockId;
import com.capgemini.hms.room.repository.BlockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BlockService {

    private final BlockRepository blockRepository;

    public BlockService(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    public List<Block> getAllBlocks() {
        return blockRepository.findAll();
    }

    public Optional<Block> getBlockById(Integer floor, Integer code) {
        return blockRepository.findById(new BlockId(floor, code));
    }

    @Transactional
    public Block saveBlock(Block block) {
        return blockRepository.save(block);
    }

    @Transactional
    public void deleteBlock(Integer floor, Integer code) {
        BlockId id = new BlockId(floor, code);
        if (!blockRepository.existsById(id)) {
            throw new RuntimeException("Block not found with Floor: " + floor + " and Code: " + code);
        }
        blockRepository.deleteById(id);
    }
}
