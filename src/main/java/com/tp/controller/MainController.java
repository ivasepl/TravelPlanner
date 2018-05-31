package com.tp.controller;

import com.tp.jpa.UsersEntity;
import com.tp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Ivan on 22.4.2018..
 */

@Controller
public class MainController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String root() {
        return "login";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String login() {
        return "login";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/register")
    public String register() {
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public String createUser(@RequestParam String firstname,
                             @RequestParam String lastname,
                             @RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String email,
                             @RequestParam String address) {

        UsersEntity user = userService.findByUsername(username);
        if (user != null) {
            return "login-error";
        }
        user = new UsersEntity();
        user.setUsername(username);
        user.setFirstName(firstname);
        user.setPassword(password);
        user.setEmail(email);
        user.setAddress(address);
        user.setLastName(lastname);
        userService.saveUser(user);
        return "login";
    }


    @RequestMapping(method = RequestMethod.GET, value = "/users/dashboard")
    public String dashboard() {
        return "users/dashboard";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login-error")
    public String error() {
        return "login-error";
    }


}

