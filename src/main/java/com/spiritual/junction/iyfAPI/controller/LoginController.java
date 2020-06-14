package com.spiritual.junction.iyfAPI.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sahil on 1/10/18.
 */
@RestController
@CrossOrigin
@RequestMapping("/account")
public class LoginController {


    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    // this is the login api/service
    @CrossOrigin
    @RequestMapping("/login")
    public Principal user(Principal principal) {
        logger.info("user logged in"+principal);
        return principal;
    }
}
