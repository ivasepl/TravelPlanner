package com.tp.repositories;

import com.tp.jpa.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<TripEntity, Integer> {
        TripEntity findByTripId(Integer id);
}
