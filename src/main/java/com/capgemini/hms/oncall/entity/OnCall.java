package com.capgemini.hms.oncall.entity;

import com.capgemini.hms.nurse.entity.Nurse;
import com.capgemini.hms.room.entity.Block;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "on_call")
public class OnCall {

    @EmbeddedId
    private OnCallId id;

    @ManyToOne
    @MapsId("nurse")
    @JoinColumn(name = "nurse", referencedColumnName = "employeeid")
    private Nurse nurse;

    @ManyToOne
    @MapsId("block")
    @JoinColumns({
        @JoinColumn(name = "blockfloor", referencedColumnName = "blockfloor"),
        @JoinColumn(name = "blockcode", referencedColumnName = "blockcode")
    })
    private Block block;

    public OnCall() {
    }

    public OnCall(OnCallId id, Nurse nurse, Block block) {
        this.id = id;
        this.nurse = nurse;
        this.block = block;
    }

    public OnCallId getId() {
        return id;
    }

    public void setId(OnCallId id) {
        this.id = id;
    }

    public Nurse getNurse() {
        return nurse;
    }

    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
