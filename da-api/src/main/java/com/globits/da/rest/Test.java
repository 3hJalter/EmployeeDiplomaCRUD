package com.globits.da.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class Test {
    @GetMapping("/hello")
    public String hello(){
        return "Xin Chào";
    }
}
