package com.tp.services;

import com.tp.jpa.AddressEntity;
import com.tp.jpa.TripEntity;

public interface GoogleApiService {
    AddressEntity getGeolocation(String address, TripEntity tripEntity);
}
