package com.epam.security.bruteforce;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.epam.security.auth.User;
import com.epam.security.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.swing.*;

@Service
public class LoginAttemptService {

    @Autowired
    UserRepository userRepository;

    private final int MAX_ATTEMPT = 3;
    private LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptService() {
        super();
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(String key) {
                return 0;
            }
        });
    }

    public void blockUser(String key) {
        User user = userRepository.findByUsername(key);
        user.setBlocked(true);
        userRepository.save(user);
        System.out.println("User " + key + "blocked");
    }

    public void unlockUser(String key) {
        User user = userRepository.findByUsername(key);
        user.setBlocked(false);
        userRepository.save(user);
        System.out.println("User " + key + "unlocked");
    }

    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
        unlockUser(key);
        System.out.println("Cash invalidated");
    }

    public void loginFailed(String key) {
        int attempts = 0;
        try {
            attempts = attemptsCache.get(key);
        } catch (final ExecutionException e) {
            attempts = 0;
        }
        if(attempts >= MAX_ATTEMPT ){
            blockUser(key);
        } else{
            attempts++;
            attemptsCache.put(key, attempts);
            System.out.println("User " + key + " use attempts " + attempts);
        }
    }
}
