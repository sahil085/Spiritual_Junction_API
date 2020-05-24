package com.spiritual.junction.iyfAPI.securityservices;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.spiritual.junction.iyfAPI.domain.User;


@Component
public class CurrentUser {


    public static User getCurrentUser()
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        User userInfo= (User)authentication.getPrincipal();

        return userInfo;
    }


}
