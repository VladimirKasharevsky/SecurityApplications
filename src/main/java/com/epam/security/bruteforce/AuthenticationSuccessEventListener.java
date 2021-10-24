package com.epam.security.bruteforce;


import com.epam.security.auth.User;
import com.epam.security.auth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent e) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            UsernamePasswordAuthenticationToken source = (UsernamePasswordAuthenticationToken) e.getSource();
            UserPrincipal userPrincipal = (UserPrincipal) source.getPrincipal();
            User user = new User();
            user.setUsername(userPrincipal.getUsername());
            loginAttemptService.loginSucceeded(user.getUsername());
        }
    }
}
