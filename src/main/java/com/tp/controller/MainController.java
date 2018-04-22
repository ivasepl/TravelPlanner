package com.tp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Ivan on 22.4.2018..
 */

@Controller
public class MainController {

    @RequestMapping ("/home")
    public String home(){
        return "index"
;    }

}

