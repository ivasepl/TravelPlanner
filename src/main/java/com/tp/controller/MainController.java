package com.tp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Ivan on 22.4.2018..
 */

@Controller
public class MainController {


    @RequestMapping (method=RequestMethod.GET, value="/")
    public String root(){

        return "login";
    }

    @RequestMapping (method=RequestMethod.GET, value="/login")
    public String login(){
        return "login";
    }

    @RequestMapping (method=RequestMethod.GET, value="/register")
    public String register(){
        return "register";
    }
    @RequestMapping (method = RequestMethod.POST, value= "/login" ,params = "signup")
    public String registration(){
        return "redirect:/register";
    }

}

