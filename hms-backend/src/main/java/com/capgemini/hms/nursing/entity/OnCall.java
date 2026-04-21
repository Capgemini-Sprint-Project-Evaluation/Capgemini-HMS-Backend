package com.capgemini.hms.nursing.entity;

import com.capgemini.hms.infrastructure.entity.Block;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "on_call")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnCall {
    @Id
    @EmbeddedId
    private OnCallId id;

    @ManyToOne
    @MapsId("nurse")
    @JoinColumn(name = "nurse", referencedColumnName = "employeeid")
    private Nurse nurse;

    @ManyToOne
    @JoinColumn(name = "blockfloor", referencedColumnName = "blockfloor", insertable = false, updatable = false)
    @JoinColumn(name = "blockcode", referencedColumnName = "blockcode", insertable = false, updatable = false)
    private Block block;


}
