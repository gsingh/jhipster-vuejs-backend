package com.pmill.vuejs.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

import com.pmill.vuejs.domain.enumeration.Shift;

/**
 * A Production.
 */
@Entity
@Table(name = "production")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Production implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "prod_date", nullable = false)
    private LocalDate prodDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "shift", nullable = false)
    private Shift shift;

    @Column(name = "no_of_plates")
    private Integer noOfPlates;

    @Column(name = "prod_tonnage")
    private Integer prodTonnage;

    @ManyToOne
    @JsonIgnoreProperties("prods")
    private ShiftManager manager;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getProdDate() {
        return prodDate;
    }

    public Production prodDate(LocalDate prodDate) {
        this.prodDate = prodDate;
        return this;
    }

    public void setProdDate(LocalDate prodDate) {
        this.prodDate = prodDate;
    }

    public Shift getShift() {
        return shift;
    }

    public Production shift(Shift shift) {
        this.shift = shift;
        return this;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Integer getNoOfPlates() {
        return noOfPlates;
    }

    public Production noOfPlates(Integer noOfPlates) {
        this.noOfPlates = noOfPlates;
        return this;
    }

    public void setNoOfPlates(Integer noOfPlates) {
        this.noOfPlates = noOfPlates;
    }

    public Integer getProdTonnage() {
        return prodTonnage;
    }

    public Production prodTonnage(Integer prodTonnage) {
        this.prodTonnage = prodTonnage;
        return this;
    }

    public void setProdTonnage(Integer prodTonnage) {
        this.prodTonnage = prodTonnage;
    }

    public ShiftManager getManager() {
        return manager;
    }

    public Production manager(ShiftManager shiftManager) {
        this.manager = shiftManager;
        return this;
    }

    public void setManager(ShiftManager shiftManager) {
        this.manager = shiftManager;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Production)) {
            return false;
        }
        return id != null && id.equals(((Production) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Production{" +
            "id=" + getId() +
            ", prodDate='" + getProdDate() + "'" +
            ", shift='" + getShift() + "'" +
            ", noOfPlates=" + getNoOfPlates() +
            ", prodTonnage=" + getProdTonnage() +
            "}";
    }
}
