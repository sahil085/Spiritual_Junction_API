package com.spiritual.junction.iyfAPI.service;


import com.spiritual.junction.iyfAPI.domain.Role;
import com.spiritual.junction.iyfAPI.domain.User;

public class RequestService {

    private Role currentRole;
    private User currentUser;

    public void setCurrentRole(Role currentRole) {
        this.currentRole = currentRole;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Role fetchCurrentRole() {
        return this.currentRole;
    }

    public User fetchCurrentUser() {
        return this.currentUser;
    }
}
