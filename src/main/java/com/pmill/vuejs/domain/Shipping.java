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
 * A Shipping.
 */
@Entity
@Table(name = "shipping")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Shipping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shipping_date", nullable = false)
    private LocalDate shippingDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "shift", nullable = false)
    private Shift shift;

    @Column(name = "no_of_wagons")
    private Integer noOfWagons;

    @Column(name = "no_of_trailers")
    private Integer noOfTrailers;

    @Column(name = "shipped_tonnage")
    private Integer shippedTonnage;

    @ManyToOne
    @JsonIgnoreProperties("shippings")
    private ShiftManager manager;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public Shipping shippingDate(LocalDate shippingDate) {
        this.shippingDate = shippingDate;
        return this;
    }

    public void setShippingDate(LocalDate shippingDate) {
        this.shippingDate = shippingDate;
    }

    public Shift getShift() {
        return shift;
    }

    public Shipping shift(Shift shift) {
        this.shift = shift;
        return this;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Integer getNoOfWagons() {
        return noOfWagons;
    }

    public Shipping noOfWagons(Integer noOfWagons) {
        this.noOfWagons = noOfWagons;
        return this;
    }

    public void setNoOfWagons(Integer noOfWagons) {
        this.noOfWagons = noOfWagons;
    }

    public Integer getNoOfTrailers() {
        return noOfTrailers;
    }

    public Shipping noOfTrailers(Integer noOfTrailers) {
        this.noOfTrailers = noOfTrailers;
        return this;
    }

    public void setNoOfTrailers(Integer noOfTrailers) {
        this.noOfTrailers = noOfTrailers;
    }

    public Integer getShippedTonnage() {
        return shippedTonnage;
    }

    public Shipping shippedTonnage(Integer shippedTonnage) {
        this.shippedTonnage = shippedTonnage;
        return this;
    }

    public void setShippedTonnage(Integer shippedTonnage) {
        this.shippedTonnage = shippedTonnage;
    }

    public ShiftManager getManager() {
        return manager;
    }

    public Shipping manager(ShiftManager shiftManager) {
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
        if (!(o instanceof Shipping)) {
            return false;
        }
        return id != null && id.equals(((Shipping) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Shipping{" +
            "id=" + getId() +
            ", shippingDate='" + getShippingDate() + "'" +
            ", shift='" + getShift() + "'" +
            ", noOfWagons=" + getNoOfWagons() +
            ", noOfTrailers=" + getNoOfTrailers() +
            ", shippedTonnage=" + getShippedTonnage() +
            "}";
    }
}
