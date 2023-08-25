package com.realm.myrealm.configurations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping
    public String hello() {
        return "Hello from Sping Boots & KeyCloak";
    }

    @GetMapping("/hello-2")
    public String hello2() {
        return "Hello from Sping Boots & KeyCloak - ADMIN";
    }
}

