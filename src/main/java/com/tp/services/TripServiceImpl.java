package com.tp.services;

import com.tp.jpa.TripEntity;
import com.tp.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripRepository tripRepository;

    @Override
    public TripEntity findByTripId(Integer id) {
        return tripRepository.findByTripId(id);
    }

    @Override
    public void saveTrip(TripEntity tripEntity) {
        tripRepository.save(tripEntity);
    }

    @Override
    public void updateTrip(TripEntity tripEntity) {
        tripRepository.save(tripEntity);
    }

    @Override
    public void deleteTrip(Integer id) {
        tripRepository.deleteById(id);
    }
}
