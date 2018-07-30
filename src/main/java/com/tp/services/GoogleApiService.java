package com.tp.services;

import com.tp.jpa.AddressEntity;
import com.tp.jpa.TripEntity;
import com.tp.jpa.UsersEntity;

public interface GoogleApiService {
    AddressEntity getGeolocation(String address, TripEntity tripEntity);
    String getUserGeolocation(UsersEntity usersEntity);
}
