package com.tp.jpa;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tp.serializers.UsersEntitySerializer;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Ivan on 22.4.2018..
 */
@JsonSerialize(using = UsersEntitySerializer.class)
@Entity
@Table(name = "USERS")
public class UsersEntity {
    private int userId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private boolean active;
    private byte[] userImage;
    private String description;


    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "USERNAME")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Lob
    @Column( name = "USERIMAGE")
    public byte[] getUserImage(){return userImage; }

    public void setUserImage(byte[] userImage) { this.userImage = userImage; }

    @Basic
    @Column(name = "ACTIVE")
    public boolean isActive() { return active; }

    public void setActive(boolean active) {this.active = active;}

    @Basic
    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
