package com.tp.services;

import com.tp.jpa.AddressEntity;

public interface AddressService {
    AddressEntity findByAddressId(Integer id);
    AddressEntity findByAddressName(String address);
    void addAddress(AddressEntity addressEntity);
    void updateAddress(AddressEntity addressEntity);
}
