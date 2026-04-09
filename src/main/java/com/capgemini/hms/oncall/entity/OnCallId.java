package com.capgemini.hms.oncall.entity;

import com.capgemini.hms.room.entity.BlockId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnCallId implements Serializable {
    @Column(name = "nurse")
    private Integer nurse;

    private BlockId block;

    @Column(name = "oncallstart")
    private LocalDateTime onCallStart;

    @Column(name = "oncallend")
    private LocalDateTime onCallEnd;
}
