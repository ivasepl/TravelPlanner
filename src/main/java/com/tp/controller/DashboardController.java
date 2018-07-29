package com.tp.controller;

import com.tp.jpa.AddressEntity;
import com.tp.jpa.TripEntity;
import com.tp.jpa.UsersEntity;
import com.tp.models.EventModel;
import com.tp.models.LocationsModel;
import com.tp.services.GoogleApiService;
import com.tp.services.TripService;
import com.tp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Console;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@RestController
public class DashboardController {

    @Autowired
    UserService userService;

    @Autowired
    GoogleApiService apiService;

    @Autowired
    TripService tripService;

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ResponseEntity<UsersEntity> userData(final Principal principal) {
        String username = principal.getName();
        UsersEntity usersEntity = null;
        if (username != null) {
            usersEntity = userService.findByUsername(username);
            return new ResponseEntity<>(usersEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/api/profile")
    public ResponseEntity userData(@RequestBody String data) {
        System.out.println(data);
        Map<String, Object> json = JsonParserFactory.getJsonParser().parseMap(data);
        UsersEntity user = userService.findByUsername(json.get("username").toString());
        if (user != null) {
            user.setFirstName(json.get("firstname").toString());
            user.setLastName(json.get("lastname").toString());
            user.setEmail(json.get("email").toString());
            user.setAddress(json.get("address").toString());
            user.setDescription(json.get("description").toString());
            if (json.get("image") != null && !json.get("image").equals("null")) {
                user.setUserImage(Base64.getDecoder().decode(json.get("image").toString()));
            }
            userService.updateUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping("/api/add_trip")
    public ResponseEntity addTrip(@RequestBody String data, Principal user) throws ParseException {
        //UsersEntity usersEntity = userService.findByUsername(user.getName());
        Map<String, Object> json = JsonParserFactory.getJsonParser().parseMap(data);
        TripEntity trip = new TripEntity();
        String address = json.get("address").toString();
        String tripName = json.get("name").toString();
        String type = json.get("type").toString();
        Timestamp dateFrom = new Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(json.get("dateFrom").toString()).getTime());
        Timestamp dateTo = new Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(json.get("dateTo").toString()).getTime());
        String details = json.get("details").toString();
        trip.setName(tripName);
        trip.setDateFrom(dateFrom);
        trip.setDateTo(dateTo);
        trip.setDetails(details);
        trip.setType(type);
        AddressEntity addressEntity = apiService.getGeolocation(address, trip);
        if (addressEntity != null) {
            if (trip.getAddress() == null) {
                Set addressSet = new HashSet();
                addressSet.add(addressEntity);
                trip.setAddress(addressSet);
            } else {
                trip.getAddress().add(addressEntity);
            }
            if (trip.getUser() == null) {
                Set userSet = new HashSet();
                // userSet.add(usersEntity);
                trip.setUser(userSet);
            } else {
                //   trip.getUser().add(usersEntity);
            }
            tripService.saveTrip(trip);
/*            if(usersEntity.getTrips() == null){
                Set tripSet = new HashSet();
                tripSet.add(trip);
                usersEntity.setTrips(tripSet);
            }else{
                usersEntity.getTrips().add(trip);
            }
            userService.updateUser(usersEntity);*/
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("api/update_trip")
    public ResponseEntity updateTrip(@RequestBody String data, Principal user) throws ParseException {
        Map<String, Object> json = JsonParserFactory.getJsonParser().parseMap(data);
        int addressId = Integer.parseInt(json.get("id").toString());
        if(addressId == 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        TripEntity trip =  tripService.findByTripId(addressId);
        if(trip == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String address = json.get("address").toString();
        String tripName = json.get("name").toString();
        String type = json.get("type").toString();
        Timestamp dateFrom = new Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(json.get("dateFrom").toString()).getTime());
        Timestamp dateTo = new Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(json.get("dateTo").toString()).getTime());
        String details = json.get("details").toString();
        trip.setName(tripName);
        trip.setDateFrom(dateFrom);
        trip.setDateTo(dateTo);
        trip.setDetails(details);
        trip.setType(type);
        AddressEntity addressEntity = apiService.getGeolocation(address, trip);
        if(addressEntity != null && !trip.getAddress().contains(addressEntity)){
            Set addressSet = new HashSet();
            addressSet.add(addressEntity);
            trip.setAddress(addressSet);
        }
        tripService.updateTrip(trip);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/api/locations")
    public ResponseEntity getLocations(Principal user){
        List<LocationsModel> locations = new ArrayList<>();
        UsersEntity usersEntity = userService.findByUsername(user.getName());
        Set<TripEntity> trips = usersEntity.getTrips();
        if(trips != null){
            for (TripEntity trip : trips) {
                LocationsModel locationsModel = new LocationsModel();
                locationsModel.setTripId(trip.getTripId());
                locationsModel.setTripName(trip.getName());
                for (AddressEntity addressEntity : trip.getAddress()) {
                    locationsModel.setLat(addressEntity.getLatitude());
                    locationsModel.setLng(addressEntity.getLangitude());
                }
                locationsModel.setDetails(trip.getDetails());
                locations.add(locationsModel);
            }
        }
        return new ResponseEntity<>(locations,HttpStatus.OK);
    }

    @RequestMapping("/api/events")
    public ResponseEntity getEvents(Principal user){
        List<EventModel> events = new ArrayList<>();
        int eventCounter = 1;
        UsersEntity usersEntity = userService.findByUsername(user.getName());
        Set<TripEntity> trips = usersEntity.getTrips();
        if(trips != null){
            for (TripEntity trip : trips) {
                EventModel eventModel = new EventModel();
                eventModel.setEventId(eventCounter);
                eventModel.setEventName(trip.getName());
                eventModel.setDateFrom(trip.getDateFrom());
                eventModel.setDateTo(trip.getDateTo());
                eventCounter++;
                events.add(eventModel);
            }
        }
        return new ResponseEntity<>(events,HttpStatus.OK);
    }
}
