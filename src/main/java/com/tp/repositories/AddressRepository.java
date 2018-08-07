package com.tp.repositories;

import com.tp.jpa.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressEntity, String> {
    AddressEntity findByAddressId(Integer id);
    AddressEntity findByAddress(String address);
}
