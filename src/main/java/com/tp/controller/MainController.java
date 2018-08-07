package com.tp.controller;

import com.tp.jpa.UsersEntity;
import com.tp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @RequestMapping(method = RequestMethod.POST, value = "/registration")
    public String createUser(@RequestParam String firstname,
                             @RequestParam String lastname,
                             @RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String email,
                             @RequestParam String address) {

        if(firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || address.isEmpty()){
            return "redirect:/register-error-input";
        }
        UsersEntity user = userService.findByUsername(username);
        if (user != null) {
            return "redirect:/register-error";
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
    public String error(final Model model) {
        model.addAttribute("errorMessage","Invalid username or password!");
        return "login";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/register-error")
    public String errorRegistration(final Model model) {
        model.addAttribute("errorRegMessage","Username already exists!");
        return "register";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/register-error-input")
    public String errorRegistrationInput(final Model model) {
        model.addAttribute("errorRegMessage","Please fill all fields!");
        return "register";
    }
    @RequestMapping(method = RequestMethod.GET, value = "/users/profile")
    public String profile() {
        return "users/profile";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/dashboard-page")
    public String dashboardPage() {
        return "users/dashboard-page";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/map")
    public String map() {
        return "users/map";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/dialog")
    public String dialog() {
        return "users/dialog";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/dialog-update")
    public String dialogUpdate() {
        return "users/dialog-update";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/dialog-delete")
    public String dialogDelete() {
        return "users/dialog-delete";
    }
}

