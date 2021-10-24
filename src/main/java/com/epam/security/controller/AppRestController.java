package com.epam.security.controller;

import com.epam.security.auth.User;
import com.epam.security.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class AppRestController {

    @Autowired
    UserRepository userRepository;

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

    @GetMapping("/blocked")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> blocked() {
        List<User> list = userRepository.findByBlockedTrue();
        return list;
    }

    @GetMapping("/unlock/{key}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String unlock(@PathVariable String key) {
        User user = userRepository.findByUsername(key);
        System.out.println(key);
        user.setBlocked(false);
        userRepository.save(user);
        return key;
    }
}
