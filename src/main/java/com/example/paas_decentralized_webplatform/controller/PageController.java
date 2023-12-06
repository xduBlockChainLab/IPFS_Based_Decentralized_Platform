package com.example.paas_decentralized_webplatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @RequestMapping("/uploadPage")
    public String uploadPage(){
       return "upload";
    }

    @RequestMapping("/checkPage")
    public String checkPage(){
        return "check";
    }

    @RequestMapping("/graph")
    public String graphPage(){
        return "graph";
    }
}
