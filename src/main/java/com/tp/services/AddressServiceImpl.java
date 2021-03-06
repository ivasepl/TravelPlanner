package com.tp.services;

import com.tp.jpa.AddressEntity;
import com.tp.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Override
    public AddressEntity findByAddressId(Integer id) {
        return addressRepository.findByAddressId(id);
    }

    @Override
    public AddressEntity findByAddressName(String name) {
        return addressRepository.findByAddress(name);
    }

    @Override
    public void addAddress(AddressEntity addressEntity) {
        addressRepository.save(addressEntity);
    }

    @Override
    public void updateAddress(AddressEntity addressEntity) {
        addressRepository.save(addressEntity);
    }
}
