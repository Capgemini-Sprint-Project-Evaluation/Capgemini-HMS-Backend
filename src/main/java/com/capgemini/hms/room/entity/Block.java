package com.capgemini.hms.room.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "block")
public class Block {

    @EmbeddedId
    private BlockId id;

    public Block() {
    }

    public Block(BlockId id) {
        this.id = id;
    }

    public BlockId getId() {
        return id;
    }

    public void setId(BlockId id) {
        this.id = id;
    }
}
