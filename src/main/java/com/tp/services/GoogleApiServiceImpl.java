package com.tp.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.jpa.AddressEntity;
import com.tp.jpa.TripEntity;
import com.tp.jpa.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class GoogleApiServiceImpl implements GoogleApiService {

    @Value("${google.api.key}")
    private String apiKey;

    @Value("${google.api.url}")
    private String apiUrl;

    @Autowired
    private AddressService addressService;

    @Autowired
    private TripService tripService;

    @Override
    public AddressEntity getGeolocation(String address, TripEntity tripEntity) {
        AddressEntity addressEntity = addressService.findByAddressName(address);
        if (addressEntity != null) {
            tripService.saveTrip(tripEntity);
            addressEntity.getTrip().add(tripEntity);
            addressService.updateAddress(addressEntity);
        } else {
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(apiUrl + "address=" + address + "&key=" + apiKey, String.class);
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode node = mapper.readTree(result);
                JsonNode latNode = node.findValue("lat");
                JsonNode lngNode = node.findValue("lng");
                if (latNode != null && lngNode != null) {
                    addressEntity = new AddressEntity();
                    addressEntity.setAddress(address);
                    addressEntity.getTrip().add(tripEntity);
                    if (addressEntity.getTrip() == null) {
                        Set set = new HashSet();
                        set.add(tripEntity);
                        addressEntity.setTrip(set);
                    } else {
                        addressEntity.getTrip().add(tripEntity);
                    }
                    addressEntity.setLangitude(lngNode.floatValue());
                    addressEntity.setLatitude(latNode.floatValue());
                    addressService.addAddress(addressEntity);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return addressEntity;
    }

    @Override
    public String getUserGeolocation(UsersEntity usersEntity) {
        if (usersEntity.getAddress() != null) {
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(apiUrl + "address=" + usersEntity.getAddress() + "&key=" + apiKey, String.class);
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode node = mapper.readTree(result);
                JsonNode latNode = node.findValue("lat");
                JsonNode lngNode = node.findValue("lng");
                if (latNode != null && lngNode != null) {
                    return "[{\"lat\": " + latNode.floatValue() + " , \"lng\":" + lngNode.floatValue() + "}]";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
