package com.sinovatio.middle.websocket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping(value = "/websocket")
    private String index(){
        return "websocket/index";
    }

}