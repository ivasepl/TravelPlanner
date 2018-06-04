package com.tp.controller;

import com.tp.jpa.UsersEntity;
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
import java.util.Base64;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class DashboardController {

    @Autowired
    UserService userService;

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
        Map<String, Object> json =  JsonParserFactory.getJsonParser().parseMap(data);
        UsersEntity user = userService.findByUsername(json.get("username").toString());
        if (user != null) {
            user.setFirstName(json.get("firstname").toString());
            user.setLastName(json.get("lastname").toString());
            user.setEmail(json.get("email").toString());
            user.setAddress(json.get("address").toString());
            user.setDescription(json.get("description").toString());
            if(json.get("image") != null && !json.get("image").equals("null")){
                user.setUserImage(Base64.getDecoder().decode(json.get("image").toString()));
            }
            userService.updateUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
