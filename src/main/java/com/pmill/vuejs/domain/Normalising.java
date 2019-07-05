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
 * A Normalising.
 */
@Entity
@Table(name = "normalising")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Normalising implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "normalising_date", nullable = false)
    private LocalDate normalisingDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "shift", nullable = false)
    private Shift shift;

    @Column(name = "no_of_plates")
    private Integer noOfPlates;

    @Column(name = "normalised_tonnage")
    private Integer normalisedTonnage;

    @ManyToOne
    @JsonIgnoreProperties("normaliseds")
    private ShiftManager manager;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getNormalisingDate() {
        return normalisingDate;
    }

    public Normalising normalisingDate(LocalDate normalisingDate) {
        this.normalisingDate = normalisingDate;
        return this;
    }

    public void setNormalisingDate(LocalDate normalisingDate) {
        this.normalisingDate = normalisingDate;
    }

    public Shift getShift() {
        return shift;
    }

    public Normalising shift(Shift shift) {
        this.shift = shift;
        return this;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Integer getNoOfPlates() {
        return noOfPlates;
    }

    public Normalising noOfPlates(Integer noOfPlates) {
        this.noOfPlates = noOfPlates;
        return this;
    }

    public void setNoOfPlates(Integer noOfPlates) {
        this.noOfPlates = noOfPlates;
    }

    public Integer getNormalisedTonnage() {
        return normalisedTonnage;
    }

    public Normalising normalisedTonnage(Integer normalisedTonnage) {
        this.normalisedTonnage = normalisedTonnage;
        return this;
    }

    public void setNormalisedTonnage(Integer normalisedTonnage) {
        this.normalisedTonnage = normalisedTonnage;
    }

    public ShiftManager getManager() {
        return manager;
    }

    public Normalising manager(ShiftManager shiftManager) {
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
        if (!(o instanceof Normalising)) {
            return false;
        }
        return id != null && id.equals(((Normalising) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Normalising{" +
            "id=" + getId() +
            ", normalisingDate='" + getNormalisingDate() + "'" +
            ", shift='" + getShift() + "'" +
            ", noOfPlates=" + getNoOfPlates() +
            ", normalisedTonnage=" + getNormalisedTonnage() +
            "}";
    }
}
