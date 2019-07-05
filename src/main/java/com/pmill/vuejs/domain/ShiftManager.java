package com.pmill.vuejs.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ShiftManager.
 */
@Entity
@Table(name = "shift_manager")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ShiftManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "designation", nullable = false)
    private String designation;

    @NotNull
    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @OneToMany(mappedBy = "manager")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Production> prods = new HashSet<>();

    @OneToMany(mappedBy = "manager")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<HeavyPlateFinished> hpFinishes = new HashSet<>();

    @OneToMany(mappedBy = "manager")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Normalising> normaliseds = new HashSet<>();

    @OneToMany(mappedBy = "manager")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Shipping> shippings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ShiftManager name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public ShiftManager designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public ShiftManager mobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Set<Production> getProds() {
        return prods;
    }

    public ShiftManager prods(Set<Production> productions) {
        this.prods = productions;
        return this;
    }

    public ShiftManager addProd(Production production) {
        this.prods.add(production);
        production.setManager(this);
        return this;
    }

    public ShiftManager removeProd(Production production) {
        this.prods.remove(production);
        production.setManager(null);
        return this;
    }

    public void setProds(Set<Production> productions) {
        this.prods = productions;
    }

    public Set<HeavyPlateFinished> getHpFinishes() {
        return hpFinishes;
    }

    public ShiftManager hpFinishes(Set<HeavyPlateFinished> heavyPlateFinisheds) {
        this.hpFinishes = heavyPlateFinisheds;
        return this;
    }

    public ShiftManager addHpFinish(HeavyPlateFinished heavyPlateFinished) {
        this.hpFinishes.add(heavyPlateFinished);
        heavyPlateFinished.setManager(this);
        return this;
    }

    public ShiftManager removeHpFinish(HeavyPlateFinished heavyPlateFinished) {
        this.hpFinishes.remove(heavyPlateFinished);
        heavyPlateFinished.setManager(null);
        return this;
    }

    public void setHpFinishes(Set<HeavyPlateFinished> heavyPlateFinisheds) {
        this.hpFinishes = heavyPlateFinisheds;
    }

    public Set<Normalising> getNormaliseds() {
        return normaliseds;
    }

    public ShiftManager normaliseds(Set<Normalising> normalisings) {
        this.normaliseds = normalisings;
        return this;
    }

    public ShiftManager addNormalised(Normalising normalising) {
        this.normaliseds.add(normalising);
        normalising.setManager(this);
        return this;
    }

    public ShiftManager removeNormalised(Normalising normalising) {
        this.normaliseds.remove(normalising);
        normalising.setManager(null);
        return this;
    }

    public void setNormaliseds(Set<Normalising> normalisings) {
        this.normaliseds = normalisings;
    }

    public Set<Shipping> getShippings() {
        return shippings;
    }

    public ShiftManager shippings(Set<Shipping> shippings) {
        this.shippings = shippings;
        return this;
    }

    public ShiftManager addShipping(Shipping shipping) {
        this.shippings.add(shipping);
        shipping.setManager(this);
        return this;
    }

    public ShiftManager removeShipping(Shipping shipping) {
        this.shippings.remove(shipping);
        shipping.setManager(null);
        return this;
    }

    public void setShippings(Set<Shipping> shippings) {
        this.shippings = shippings;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShiftManager)) {
            return false;
        }
        return id != null && id.equals(((ShiftManager) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ShiftManager{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", mobileNumber='" + getMobileNumber() + "'" +
            "}";
    }
}
