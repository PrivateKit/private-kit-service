package com.privatekit.services.filter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CaptchaFilterMockController {

    @GetMapping("/")
    public String index() {
        return "Hello World!";
    }
}
