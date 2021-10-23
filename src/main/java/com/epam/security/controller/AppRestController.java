package com.epam.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AppRestController {

    @GetMapping("/info")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String info() {
        return "INFO";
    }

    @GetMapping("/about")
    public String about() {
        return "ABOUT";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin() {
        return "ADMIN";
    }
}
