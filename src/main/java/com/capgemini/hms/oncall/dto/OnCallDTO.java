package com.capgemini.hms.oncall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class OnCallDTO {
    @NotNull
    @Schema(example = "501", description = "Employee ID of the nurse on call")
    private Integer nurseId;

    @NotNull
    @Schema(example = "1", description = "Floor number where the block is located")
    private Integer blockFloor;

    @NotNull
    @Schema(example = "1", description = "Unique code for the block (ward/area)")
    private Integer blockCode;

    @NotNull
    @Schema(example = "2026-04-10T18:00:00", description = "Start time of the on-call shift")
    private LocalDateTime onCallStart;

    @NotNull
    @Schema(example = "2026-04-11T06:00:00", description = "End time of the on-call shift")
    private LocalDateTime onCallEnd;

    public OnCallDTO() {}

    public OnCallDTO(Integer nurseId, Integer blockFloor, Integer blockCode, 
                     LocalDateTime onCallStart, LocalDateTime onCallEnd) {
        this.nurseId = nurseId;
        this.blockFloor = blockFloor;
        this.blockCode = blockCode;
        this.onCallStart = onCallStart;
        this.onCallEnd = onCallEnd;
    }

    public Integer getNurseId() { return nurseId; }
    public void setNurseId(Integer nurseId) { this.nurseId = nurseId; }

    public Integer getBlockFloor() { return blockFloor; }
    public void setBlockFloor(Integer blockFloor) { this.blockFloor = blockFloor; }

    public Integer getBlockCode() { return blockCode; }
    public void setBlockCode(Integer blockCode) { this.blockCode = blockCode; }

    public LocalDateTime getOnCallStart() { return onCallStart; }
    public void setOnCallStart(LocalDateTime onCallStart) { this.onCallStart = onCallStart; }

    public LocalDateTime getOnCallEnd() { return onCallEnd; }
    public void setOnCallEnd(LocalDateTime onCallEnd) { this.onCallEnd = onCallEnd; }
}
