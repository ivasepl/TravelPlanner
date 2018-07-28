package com.tp.jpa;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "TRIP")
public class TripEntity {

    private int tripId;
    private String name;
    private String type;
    private Timestamp dateFrom;
    private Timestamp dateTo;
    private String details;
    private Set<AddressEntity> address;
    private Set<UsersEntity> user;

    @Id
    @Column(name = "TRIP_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    @Basic
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "DATEFROM")
    public Timestamp getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Timestamp dateFrom) {
        this.dateFrom = dateFrom;
    }

    @Basic
    @Column(name = "DATETO")
    public Timestamp getDateTo() {
        return dateTo;
    }

    public void setDateTo(Timestamp dateTo) {
        this.dateTo = dateTo;
    }

    @Basic
    @Column(name = "DETAILS")
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "TRIPLOCATION", joinColumns = {@JoinColumn(name = "TRIP_ID")}, inverseJoinColumns = {@JoinColumn(name = "ADDRESS_ID")})
    public Set<AddressEntity> getAddress() {
        return address;
    }

    public void setAddress(Set<AddressEntity> address) {
        this.address = address;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "trips")
    public Set<UsersEntity> getUser() {
        return user;
    }

    public void setUser(Set<UsersEntity> user) {
        this.user = user;
    }
}
