package com.tp.services;

import com.tp.jpa.AddressEntity;

public interface AddressService {
    AddressEntity findByAddressId(Integer id);
    void addAddress(AddressEntity addressEntity);
}
