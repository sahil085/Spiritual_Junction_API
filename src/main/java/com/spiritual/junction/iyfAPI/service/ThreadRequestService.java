package com.spiritual.junction.iyfAPI.service;

import java.util.Optional;

import com.spiritual.junction.iyfAPI.domain.Role;
import com.spiritual.junction.iyfAPI.domain.User;


public class ThreadRequestService {
    private static ThreadLocal<RequestService> requestServiceThreadLocal = ThreadLocal.withInitial(RequestService::new);

    public static void setCurrentRole(Role currentRole) {
        get().setCurrentRole(currentRole);
    }

    private static RequestService get() {
        return requestServiceThreadLocal.get();
    }

    public static void setCurrentUser(User currentUser) {
        get().setCurrentUser(currentUser);
    }

    public static Role fetchCurrentRole() {
        return get().fetchCurrentRole();
    }

    public static User fetchCurrentUser() {
        return Optional.ofNullable(get().fetchCurrentUser()).orElse(new User());
    }

    public static void remove() {
        requestServiceThreadLocal.remove();
    }
}
