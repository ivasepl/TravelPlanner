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

    @RequestMapping (method=RequestMethod.GET, value="/index")
    public String home(){
        return "index";
    }

    @RequestMapping (method=RequestMethod.GET, value="/")
    public String root(){

        return "index";
    }

    @RequestMapping (method=RequestMethod.GET, value="/login")
    public String login(){
        return "login";
    }

}

