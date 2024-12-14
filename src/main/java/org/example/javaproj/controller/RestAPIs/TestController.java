package org.example.javaproj.controller.RestAPIs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public String Welcome() {
        return "Hello world";
    }
}