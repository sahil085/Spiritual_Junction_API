package com.spiritual.junction.iyfAPI.securityConfig.jwt;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spiritual.junction.iyfAPI.domain.Role;
import com.spiritual.junction.iyfAPI.domain.User;
import com.spiritual.junction.iyfAPI.exception.ExceptionResponse;
import com.spiritual.junction.iyfAPI.exception.UnAuthorizeException;
import com.spiritual.junction.iyfAPI.repository.RoleRepository;
import com.spiritual.junction.iyfAPI.repository.UserRepository;
import com.spiritual.junction.iyfAPI.service.ThreadRequestService;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private JwtTokenUtil          jwtTokenUtil;
    @Autowired
    private RoleRepository        roleRepository;
    @Autowired
    private UserRepository        userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException, UnAuthorizeException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
// JWT Token is in the form "Bearer token". Remove Bearer word and get
// only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (Exception e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
// Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
// if token is valid configure Spring Security to manually set
// authentication
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
// After setting the Authentication in the context, we specify
// that the current user is authenticated. So it passes the
// Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        try {
            storeCurrentRequestProperties(request, username);
            chain.doFilter(request, response);
        }catch (UnAuthorizeException e){
            ExceptionResponse exceptionResponse =
                    new ExceptionResponse(new Date(), e.getMessage(), "");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(exceptionResponse));
        }
    }

    private void storeCurrentRequestProperties(HttpServletRequest request, String username) {

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            String role = request.getParameter("currentRole");
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
            GrantedAuthority grantedAuthority = userDetails.getAuthorities().stream().filter(auth -> {
                return ((GrantedAuthority) auth).getAuthority().equals(role);
            }).findFirst().orElse(null);
            if(grantedAuthority != null){
                Role currentRole = roleRepository.findByRole(role);
                ThreadRequestService.setCurrentRole(currentRole);
                User user = userRepository.findByEmail(username);
                ThreadRequestService.setCurrentUser(user);
            }else {
                /* Throwing this exception here to globally manage Unauthorized Exception After this there is no need to check on each request whether the
                current role is
                assigned to user or not*/
                throw new UnAuthorizeException("");
            }
        }
    }
}
