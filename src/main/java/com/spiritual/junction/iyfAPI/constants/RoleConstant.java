package com.spiritual.junction.iyfAPI.constants;

import java.util.Arrays;
import java.util.List;

public interface RoleConstant {

    String ROLE_SUPER_ADMIN = "SUPER ADMIN";
    String ROLE_ADMIN       = "ADMIN";
    String ROLE_USER        = "USER";

    List<String> ROLES = Arrays.asList(ROLE_SUPER_ADMIN, ROLE_ADMIN, ROLE_USER);


}
