package com.tp.jpa;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ADDRESS")
public class AddressEntity {

    private int addressId;
    private String address;
    private float latitude;
    private float langitude;
    private Set<TripEntity> trip;

    @Id
    @Column(name = "ADDRESS_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    @Basic
    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "LATITUDE")
    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "LONGITUDE")
    public float getLangitude() {
        return langitude;
    }

    public void setLangitude(float langitude) {
        this.langitude = langitude;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "address")
    public Set<TripEntity> getTrip() {
        return trip;
    }

    public void setTrip(Set<TripEntity> trip) {
        this.trip = trip;
    }
}
