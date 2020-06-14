package com.spiritual.junction.iyfAPI.securityConfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLogin {

    private String email;
    private String photoUrl;
    private String name;
    private String provider;
}
