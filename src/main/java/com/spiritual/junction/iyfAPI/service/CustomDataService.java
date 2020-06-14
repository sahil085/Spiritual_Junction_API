package com.spiritual.junction.iyfAPI.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spiritual.junction.iyfAPI.co.UserDetailsCO;
import com.spiritual.junction.iyfAPI.constants.AppConst;
import com.spiritual.junction.iyfAPI.constants.RoleConstant;
import com.spiritual.junction.iyfAPI.domain.Role;
import com.spiritual.junction.iyfAPI.domain.User;
import com.spiritual.junction.iyfAPI.repository.RoleRepository;
import com.spiritual.junction.iyfAPI.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomDataService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    public void createUserData(List<UserDetailsCO> userDetailsCOList) {
        Role role = roleRepository.findByRole(RoleConstant.ROLE_USER);
        List<User> userList = new ArrayList<>();
        userDetailsCOList.forEach(userDetailsCO -> {
            log.info("User :: " + userDetailsCO.getEmail() + "  ::  " + userDetailsCO.getUsername());

            User userInfo = new User();
            userInfo.setEmail(userDetailsCO.getEmail());
            userInfo.setUsername(userDetailsCO.getFirstName() + userDetailsCO.getLastName());
            userInfo.setCollege(userDetailsCO.getCollegeName());
            userInfo.setAreaOfResidence(userDetailsCO.getAreaOfResidence());
            userInfo.setGender(userDetailsCO.getGender());
            userInfo.setFirstName(userDetailsCO.getFirstName());
            userInfo.setLastName(userDetailsCO.getLastName());
            userInfo.setProfession(userDetailsCO.getProfession());
            if (userDetailsCO.getPhoneNumber() != null) {
                userInfo.setNumber(userDetailsCO.getPhoneNumber());
            }
            if (userDetailsCO.getMobile() != null) {
                userInfo.setNumber(userDetailsCO.getMobile());
            }
            userInfo.setProvider(AppConst.Providers.CUSTOM);
            userInfo.setAge(userDetailsCO.getAge());
            userInfo.setPassword(new BCryptPasswordEncoder().encode(getDefaultPassword(userDetailsCO)));
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            userInfo.setRoles(roles);
            userList.add(userInfo);
        });
        userRepository.saveAll(userList);
        log.info("Data saved successfully");
    }

    private String getDefaultPassword(UserDetailsCO userDetailsCO) {
        return userDetailsCO.getEmail().substring(0, 4) + userDetailsCO.getFirstName().substring(0, 2) + "default";
    }
}
