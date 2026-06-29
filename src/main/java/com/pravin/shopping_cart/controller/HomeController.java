package com.pravin.shopping_cart.controller;

import com.pravin.shopping_cart.entities.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String hompage(Model modal){
        modal.addAttribute("name", "Pravin");

        return "Hello Pravin";
    }

    @GetMapping("/home")
    public Message homemessage(){
        return new Message("Hello world");
    }

}
