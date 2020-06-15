package com.spiritual.junction.iyfAPI.controller;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.validation.Valid;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spiritual.junction.iyfAPI.co.UserDetailsCO;
import com.spiritual.junction.iyfAPI.service.CustomDataService;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequestMapping("/data")
@Slf4j
public class CustomDataController {

    @Autowired
    private CustomDataService customDataService;

    @PostMapping(value = "/create-user")
    public void uploadUserData(@Valid  @RequestBody List<UserDetailsCO> userDetailsCOList) {
        customDataService.createUserData(userDetailsCOList);
    }

    @PostMapping("/create-participant-session")
    public void createParticipantsSessionData(@Valid @RequestBody List<UserDetailsCO> userDetailsCOList) {
        customDataService.uploadParticipantsDataWithSession(userDetailsCOList);

    }



    @GetMapping("/sendCirtificates")
    public void send() throws IOException, MessagingException {
        customDataService.send();
    }




}
