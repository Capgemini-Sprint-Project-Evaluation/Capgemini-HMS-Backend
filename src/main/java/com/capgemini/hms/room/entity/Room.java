package com.capgemini.hms.room.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "room")
public class Room {

    @Id
    @Column(name = "roomnumber")
    private Integer roomNumber;

    @Column(name = "roomtype", nullable = false)
    private String roomType;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "blockfloor", referencedColumnName = "blockfloor"),
        @JoinColumn(name = "blockcode", referencedColumnName = "blockcode")
    })
    private Block block;

    @Column(name = "unavailable", nullable = false)
    private Boolean unavailable;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public Room() {
    }

    public Room(Integer roomNumber, String roomType, Block block, Boolean unavailable) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.block = block;
        this.unavailable = unavailable;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Boolean getUnavailable() {
        return unavailable;
    }

    public void setUnavailable(Boolean unavailable) {
        this.unavailable = unavailable;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
