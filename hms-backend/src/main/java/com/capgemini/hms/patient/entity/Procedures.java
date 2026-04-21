package com.capgemini.hms.patient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "procedures")
public class Procedures {
    @Id
    private Integer code;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal cost;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public Procedures() {}
    public Procedures(Integer code, String name, java.math.BigDecimal cost, Boolean isDeleted) {
        this.code = code;
        this.name = name;
        this.cost = cost;
        this.isDeleted = isDeleted != null ? isDeleted : false;
    }

    // Manual Accessors
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public java.math.BigDecimal getCost() { return cost; }
    public void setCost(java.math.BigDecimal cost) { this.cost = cost; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    // Manual Builder
    public static class ProceduresBuilder {
        private Integer code;
        private String name;
        private java.math.BigDecimal cost;
        private Boolean isDeleted = false;
        public ProceduresBuilder code(Integer code) { this.code = code; return this; }
        public ProceduresBuilder name(String name) { this.name = name; return this; }
        public ProceduresBuilder cost(java.math.BigDecimal cost) { this.cost = cost; return this; }
        public ProceduresBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }
        public Procedures build() { return new Procedures(code, name, cost, isDeleted); }
    }
    public static ProceduresBuilder builder() { return new ProceduresBuilder(); }
}
