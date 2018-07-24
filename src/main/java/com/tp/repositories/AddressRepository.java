package com.tp.repositories;

import com.tp.jpa.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressEntity, Integer> {
    AddressEntity findByAddressId(Integer id);
}
