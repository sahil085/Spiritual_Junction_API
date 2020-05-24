package com.spiritual.junction.iyfAPI.securityservices;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spiritual.junction.iyfAPI.domain.Role;
import com.spiritual.junction.iyfAPI.domain.User;
import com.spiritual.junction.iyfAPI.repository.UserRepository;

/**
 * This Service class for providing the user credentials from the database.
 *
 * @author kamal berriga
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService    userService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println(" --------------  "+username);
//        User user = userService.find(username);
//        System.out.println(" --- "+user);
//      return user;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=  null;
        try {
            user = userRepository.findByEmail(username);
            System.out.println(user.getEmail());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        if(user == null){
            throw new UsernameNotFoundException("User does not exists");
        }
        List<SimpleGrantedAuthority> auths = new java.util.ArrayList<SimpleGrantedAuthority>();
        Set<Role> roleSet=user.getRoles();
        roleSet.forEach(role ->
                auths.add(new SimpleGrantedAuthority(role.getRole())
        )
        );
        user.setRoles(roleSet);
        return user;
    }

}
