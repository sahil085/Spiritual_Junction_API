package com.spiritual.junction.iyfAPI.securityConfig.jwt;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;

@Getter
class JwtResponse implements Serializable {
    private static final long         serialVersionUID = -8091879091924046844L;
    private              String       jwtToken;
    private              String       userName;
    private              List<String> authorities;

    JwtResponse(String jwtToken, List<String> authorities, String userName) {
        this.jwtToken = jwtToken;
        this.authorities = authorities;
        this.userName = userName;
    }


}
