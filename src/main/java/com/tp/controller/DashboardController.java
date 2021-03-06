package com.tp.controller;

import com.tp.jpa.AddressEntity;
import com.tp.jpa.TripEntity;
import com.tp.jpa.UsersEntity;
import com.tp.models.EventModel;
import com.tp.models.LocationsModel;
import com.tp.models.StatisticModel;
import com.tp.services.AddressService;
import com.tp.services.GoogleApiService;
import com.tp.services.TripService;
import com.tp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class DashboardController {

    private final String AUTUMN = "autumn";
    private final String SPRING = "spring";
    private final String WINTER = "winter";
    private final String SUMMER = "summer";

    @Autowired
    UserService userService;

    @Autowired
    GoogleApiService apiService;

    @Autowired
    TripService tripService;

    @Autowired
    AddressService addressService;

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

    @RequestMapping(value = "/api/add_trip", method = RequestMethod.POST)
    public ResponseEntity addTrip(@RequestBody String data, Principal user) throws ParseException {
        UsersEntity usersEntity = userService.findByUsername(user.getName());
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
                userSet.add(usersEntity);
                trip.setUser(userSet);
            } else {
                trip.getUser().add(usersEntity);
            }
            tripService.updateTrip(trip);
            if (usersEntity.getTrips() == null) {
                Set tripSet = new HashSet();
                tripSet.add(trip);
                usersEntity.setTrips(tripSet);
            } else {
                usersEntity.getTrips().add(trip);
            }
            userService.updateUser(usersEntity);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("api/update_trip")
    public ResponseEntity updateTrip(@RequestBody String data, Principal user) throws ParseException {
        Map<String, Object> json = JsonParserFactory.getJsonParser().parseMap(data);
        int addressId = Integer.parseInt(json.get("id").toString());
        if (addressId == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        TripEntity trip = tripService.findByTripId(addressId);
        if (trip == null) {
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
        if (addressEntity != null && !trip.getAddress().contains(addressEntity)) {
            Set addressSet = new HashSet();
            addressSet.add(addressEntity);
            trip.setAddress(addressSet);
        }
        tripService.updateTrip(trip);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/api/locations")
    public ResponseEntity getLocations(Principal user) {
        List<LocationsModel> locations = new ArrayList<>();
        UsersEntity usersEntity = userService.findByUsername(user.getName());
        Set<TripEntity> trips = usersEntity.getTrips();
        if (trips != null) {
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
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @RequestMapping("/api/events")
    public ResponseEntity getEvents(Principal user) {
        List<EventModel> events = new ArrayList<>();
        int eventCounter = 1;
        UsersEntity usersEntity = userService.findByUsername(user.getName());
        Set<TripEntity> trips = usersEntity.getTrips();
        if (trips != null) {
            for (TripEntity trip : trips) {
                EventModel eventModel = new EventModel();
                eventModel.setId(eventCounter);
                eventModel.setText(trip.getName());
                eventModel.setStart_date(new SimpleDateFormat("MM/dd/YYYY HH:mm").format(new Date(trip.getDateFrom().getTime())));
                eventModel.setEnd_date(new SimpleDateFormat("MM/dd/YYYY HH:mm").format(new Date(trip.getDateTo().getTime())));
                eventModel.setColor("#FAA71B");
                eventCounter++;
                events.add(eventModel);
            }
        }
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @RequestMapping("/api/user_location")
    public ResponseEntity getUserLocation(Principal user) {
        if (user != null) {
            UsersEntity usersEntity = userService.findByUsername(user.getName());
            String result = apiService.getUserGeolocation(usersEntity);
            if (result != null) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping("api/user_trips")
    public ResponseEntity getUserTrips(Principal user) {
        if (user != null) {
            UsersEntity usersEntity = userService.findByUsername(user.getName());
            Set<TripEntity> userTrips = usersEntity.getTrips();
            List<TripEntity> trips = new ArrayList<>();
            for (TripEntity userTrip : userTrips) {
                trips.add(userTrip);
            }
            return new ResponseEntity<>(trips, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @RequestMapping("api/get_trip")
    public ResponseEntity getTrip(@RequestBody String data) {
        Map<String, Object> json = JsonParserFactory.getJsonParser().parseMap(data);
        int id = Integer.parseInt(json.get("id").toString());

        TripEntity trip = tripService.findByTripId(id);
        if (trip != null) {
            return new ResponseEntity<>(trip, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping("api/delete_trip")
    public ResponseEntity deleteTrip(@RequestBody String data, Principal user) {
        UsersEntity usersEntity = userService.findByUsername(user.getName());
        Map<String, Object> json = JsonParserFactory.getJsonParser().parseMap(data);
        int id = Integer.parseInt(json.get("id").toString());
        TripEntity trip = tripService.findByTripId(id);
        usersEntity.getTrips().remove(trip);
        Set<AddressEntity> address = trip.getAddress();
        for (AddressEntity addressEntity : address) {
            addressEntity.getTrip().remove(trip);
            addressService.updateAddress(addressEntity);
        }
        trip.getAddress().clear();
        trip.getUser().clear();
        userService.updateUser(usersEntity);
        tripService.updateTrip(trip);
        tripService.deleteTrip(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping("api/statistic")
    public ResponseEntity statistic(Principal user) {
        UsersEntity usersEntity = userService.findByUsername(user.getName());
        Set<TripEntity> trips = usersEntity.getTrips();
        StatisticModel statisticModel = new StatisticModel();
        Calendar cal = Calendar.getInstance();
        for (TripEntity trip : trips) {
            cal.setTimeInMillis(trip.getDateFrom().getTime());
            String season = getSeasonName(cal);
            switch (season) {
                case AUTUMN:
                    statisticModel.setAutumn(statisticModel.getAutumn() + 1);
                    break;
                case WINTER:
                    statisticModel.setWinter(statisticModel.getWinter() + 1);
                    break;
                case SPRING:
                    statisticModel.setSpring(statisticModel.getSpring() + 1);
                    break;
                case SUMMER:
                    statisticModel.setSummer(statisticModel.getSummer() + 1);
                    break;
                default:
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(statisticModel, HttpStatus.OK);
    }

    private String getSeasonName(Calendar cal) {
        switch (cal.get(Calendar.MONTH)) {
            case 1:
            case 2:
                return WINTER;
            case 3:
                if (cal.get(Calendar.DAY_OF_MONTH) >= 21) {
                    return SPRING;
                } else {
                    return WINTER;
                }
            case 4:
            case 5:
                return SPRING;
            case 6:
                if (cal.get(Calendar.DAY_OF_MONTH) >= 21) {
                    return SUMMER;
                } else {
                    return SPRING;
                }
            case 7:
            case 8:
                return SUMMER;
            case 9:
                if (cal.get(Calendar.DAY_OF_MONTH) >= 23) {
                    return AUTUMN;
                } else {
                    return SUMMER;
                }
            case 10:
            case 11:
                return AUTUMN;
            case 12:
                if (cal.get(Calendar.DAY_OF_MONTH) >= 21) {
                    return WINTER;
                } else {
                    return AUTUMN;
                }
        }
        return null;
    }
}
