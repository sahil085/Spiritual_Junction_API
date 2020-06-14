package com.spiritual.junction.iyfAPI.securityConfig.jwt;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spiritual.junction.iyfAPI.constants.RoleConstant;
import com.spiritual.junction.iyfAPI.domain.Role;
import com.spiritual.junction.iyfAPI.domain.User;
import com.spiritual.junction.iyfAPI.exception.UnAuthorizeException;
import com.spiritual.junction.iyfAPI.repository.RoleRepository;
import com.spiritual.junction.iyfAPI.repository.UserRepository;
import com.spiritual.junction.iyfAPI.securityConfig.SocialLogin;
import lombok.extern.slf4j.Slf4j;


@RestController
@CrossOrigin
@Slf4j
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil          jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private UserRepository        userRepository;
    @Autowired
    private RoleRepository        roleRepository;

    @Value("${oauth.cookie.name}")
    private String oAuthCookieName;
    @Value("${oauth.cookie.domain}")
    private String oAuthCookieDomain;

    @PostMapping("/social-login")
    public ResponseEntity<JwtResponse> socialAuthentication(@NotNull @RequestBody SocialLogin socialLogin, HttpServletResponse response,
                                                            HttpServletRequest request) throws Exception {
        User user = userRepository.findByEmail(socialLogin.getEmail());

        if (user == null) {
            user = createNewUserWithDefaultPassword(socialLogin);
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userRepository.saveAndFlush(user);
            log.info("New User Created from social login");
        }
        JwtRequest jwtRequest = JwtRequest.builder()
                .password(user.getPassword())
                .username(user.getEmail()).build();

        return createAuthenticationToken(jwtRequest, response, request);

    }

    private User createNewUserWithDefaultPassword(SocialLogin socialLogin) {
        User user = User.builder().email(socialLogin.getEmail())
                .username(socialLogin.getName())
                .password(getDefaultPassword(socialLogin))
                .provider(socialLogin.getProvider()).build();
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByRole(RoleConstant.ROLE_USER);
        roles.add(role);
        user.setRoles(roles);
        return user;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest, HttpServletResponse response,
                                                                 HttpServletRequest request) throws Exception {

        if (request.getServletPath().contains("social-login")) {
            socialLoginAuthentication(authenticationRequest.getUsername());
        } else {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        List<String> authorities = userDetails.getAuthorities().stream().map(o -> ((GrantedAuthority) o).getAuthority()).collect(Collectors.toList());
        updateAuthCookie(response, token, Integer.MAX_VALUE);
        return ResponseEntity.ok(new JwtResponse(token, authorities, userDetails.getUsername()));
    }

    private String getDefaultPassword(SocialLogin socialLogin) {
        return socialLogin.getEmail().substring(0, 4) + socialLogin.getName().substring(0, 2) + "defaultPWD";
    }

    private void socialLoginAuthentication(String username) throws Exception {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(username);
            if (user != null) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(authentication);
            } else {
                throw new BadCredentialsException("");
            }

        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new UnAuthorizeException("INVALID_CREDENTIALS");
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new UnAuthorizeException("INVALID_CREDENTIALS");
        }
    }

    private void updateAuthCookie(HttpServletResponse response, String access_token, int age) {
        Cookie myCookie = new Cookie(oAuthCookieName, access_token);
        myCookie.setDomain(oAuthCookieDomain);
        myCookie.setPath("/");
        myCookie.setMaxAge(age);
        response.addCookie(myCookie);
    }

    @RequestMapping(value = "/endSession",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, String> logout(HttpServletRequest request, HttpServletResponse response) {
        removeAuthCookie(response);
        SecurityContextHolder.getContext().setAuthentication(null);
        return Collections.singletonMap("result", "SUCCESS");
    }

    public void removeAuthCookie(HttpServletResponse response) {
        updateAuthCookie(response, null, 0);
    }

}
