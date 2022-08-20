package com.lxh.controller;

import com.lxh.annotation.RateLimiter;
import com.lxh.enums.LimitType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    @RateLimiter(time = 10,count = 3,limitType = LimitType.IP)
    public String hello(){
        return "hello!rate_limiter";
    }

}
