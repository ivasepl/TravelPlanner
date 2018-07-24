package com.tp.services;

import com.tp.jpa.TripEntity;

public interface TripService {
    TripEntity findByTripId(Integer id);
    void saveTrip(TripEntity tripEntity);
    void updateTrip(TripEntity tripEntity);
}
