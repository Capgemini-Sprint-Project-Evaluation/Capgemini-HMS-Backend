package com.capgemini.hms.oncall.entity;


import com.capgemini.hms.room.entity.BlockId;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class OnCallId implements Serializable {
    @Column(name = "nurse")
    private Integer nurse;

    @Column(name = "blockfloor")
    private Integer blockFloor;

    @Column(name = "blockcode")
    private Integer blockCode;

    @Column(name = "oncallstart")
    private LocalDateTime onCallStart;

    @Column(name = "oncallend")
    private LocalDateTime onCallEnd;

    // Constructors
    public OnCallId() {}

    public OnCallId(Integer nurse, Integer blockFloor, Integer blockCode, LocalDateTime onCallStart, LocalDateTime onCallEnd) {
        this.nurse = nurse;
        this.blockFloor = blockFloor;
        this.blockCode = blockCode;
        this.onCallStart = onCallStart;
        this.onCallEnd = onCallEnd;
    }

    // Getters and Setters
    public Integer getNurse() {
        return nurse;
    }

    public void setNurse(Integer nurse) {
        this.nurse = nurse;
    }

    public Integer getBlockFloor() {
        return blockFloor;
    }

    public void setBlockFloor(Integer blockFloor) {
        this.blockFloor = blockFloor;
    }

    public Integer getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(Integer blockCode) {
        this.blockCode = blockCode;
    }

    public LocalDateTime getOnCallStart() {
        return onCallStart;
    }

    public void setOnCallStart(LocalDateTime onCallStart) {
        this.onCallStart = onCallStart;
    }

    public LocalDateTime getOnCallEnd() {
        return onCallEnd;
    }

    public void setOnCallEnd(LocalDateTime onCallEnd) {
        this.onCallEnd = onCallEnd;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnCallId onCallId = (OnCallId) o;
        return Objects.equals(nurse, onCallId.nurse) &&
               Objects.equals(blockFloor, onCallId.blockFloor) &&
               Objects.equals(blockCode, onCallId.blockCode) &&
               Objects.equals(onCallStart, onCallId.onCallStart) &&
               Objects.equals(onCallEnd, onCallId.onCallEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nurse, blockFloor, blockCode, onCallStart, onCallEnd);
    }
}