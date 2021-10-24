package com.epam.security.service;

import com.epam.security.auth.*;
import com.epam.security.bruteforce.LoginAttemptService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    private final AuthGroupRepository authGroupRepository;
    private final LoginAttemptService loginAttemptService;

    public TaskUserDetailsService(UserRepository userRepository, AuthGroupRepository authGroupRepository, LoginAttemptService loginAttemptService) {
        super();
        this.userRepository = userRepository;
        this.authGroupRepository = authGroupRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException("cannot find username: " + username);
        }
        if (user.isBlocked()) {
            throw new RuntimeException("blocked");
        }

        List<AuthGroup> authGroups = this.authGroupRepository.findByUsername(username);
        return new UserPrincipal(user, authGroups);
    }
}
