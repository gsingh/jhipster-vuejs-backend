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
 * A HeavyPlateFinished.
 */
@Entity
@Table(name = "heavy_plate_finished")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HeavyPlateFinished implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "h_p_finished_date", nullable = false)
    private LocalDate hPFinishedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "shift", nullable = false)
    private Shift shift;

    @Column(name = "no_of_plates")
    private Integer noOfPlates;

    @Column(name = "h_p_finished_tonnage")
    private Integer hPFinishedTonnage;

    @ManyToOne
    @JsonIgnoreProperties("hpFinishes")
    private ShiftManager manager;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate gethPFinishedDate() {
        return hPFinishedDate;
    }

    public HeavyPlateFinished hPFinishedDate(LocalDate hPFinishedDate) {
        this.hPFinishedDate = hPFinishedDate;
        return this;
    }

    public void sethPFinishedDate(LocalDate hPFinishedDate) {
        this.hPFinishedDate = hPFinishedDate;
    }

    public Shift getShift() {
        return shift;
    }

    public HeavyPlateFinished shift(Shift shift) {
        this.shift = shift;
        return this;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Integer getNoOfPlates() {
        return noOfPlates;
    }

    public HeavyPlateFinished noOfPlates(Integer noOfPlates) {
        this.noOfPlates = noOfPlates;
        return this;
    }

    public void setNoOfPlates(Integer noOfPlates) {
        this.noOfPlates = noOfPlates;
    }

    public Integer gethPFinishedTonnage() {
        return hPFinishedTonnage;
    }

    public HeavyPlateFinished hPFinishedTonnage(Integer hPFinishedTonnage) {
        this.hPFinishedTonnage = hPFinishedTonnage;
        return this;
    }

    public void sethPFinishedTonnage(Integer hPFinishedTonnage) {
        this.hPFinishedTonnage = hPFinishedTonnage;
    }

    public ShiftManager getManager() {
        return manager;
    }

    public HeavyPlateFinished manager(ShiftManager shiftManager) {
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
        if (!(o instanceof HeavyPlateFinished)) {
            return false;
        }
        return id != null && id.equals(((HeavyPlateFinished) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "HeavyPlateFinished{" +
            "id=" + getId() +
            ", hPFinishedDate='" + gethPFinishedDate() + "'" +
            ", shift='" + getShift() + "'" +
            ", noOfPlates=" + getNoOfPlates() +
            ", hPFinishedTonnage=" + gethPFinishedTonnage() +
            "}";
    }
}
