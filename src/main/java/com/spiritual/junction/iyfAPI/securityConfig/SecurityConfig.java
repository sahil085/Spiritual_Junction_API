package com.spiritual.junction.iyfAPI.securityConfig;//package com.iskcon.EthicraftAPI.securityConfig;
//
//import javax.annotation.PostConstruct;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//import com.iskcon.EthicraftAPI.constants.RoleConstant;
//import com.iskcon.EthicraftAPI.domain.Role;
//import com.iskcon.EthicraftAPI.domain.User;
//import com.iskcon.EthicraftAPI.domain.UserRoleCollegeMapping;
//import com.iskcon.EthicraftAPI.repository.RoleRepository;
//import com.iskcon.EthicraftAPI.repository.UserRepository;
//import com.iskcon.EthicraftAPI.repository.UserRoleCollegeRepo;
//import com.iskcon.EthicraftAPI.securityservices.CustomUserDetailsService;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true,jsr250Enabled = true,prePostEnabled = true)
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    CustomUserDetailsService appUserDetailsService;
//
//    @Autowired
//    UserRepository userInfoRepository;
//
//    @Autowired
//    RoleRepository roleRepository;
//
//
//    @Autowired
//    UserRoleCollegeRepo userRoleCollegeRepo;
//
//    @Bean
//    public ModelMapper modelMapper() {
//        return new ModelMapper();
//    }
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        return bCryptPasswordEncoder;
//    }
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(appUserDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        super.configure(web);
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider());
//    }
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**").allowedOrigins("*");
//
//            }
//        };
//    }
//
//    @PostConstruct
//    public void populateRole(){
//      List<String> roles = RoleConstant.ROLES;
//      roles.forEach(role -> {
//          Role dbRole = roleRepository.findByRole(role);
//          if(dbRole == null) {
//              Role role1 = new Role();
//              role1.setRole(role);
//              roleRepository.saveAndFlush(role1);
//          }
//
//      });
//      createAdmin();
//    }
//
//    public void createAdmin(){
//        User userInfo1 = userInfoRepository.findByEmail("sahil.verma@tothenew.com");
//        if(userInfo1==null){
//            User userInfo = new User();
//            userInfo.setEmail("sahil.verma@tothenew.com");
//            userInfo.setPassword(new BCryptPasswordEncoder().encode("igdefault"));
//            userInfo.setUsername("sahil verma");
//            Set<Role> roles = new HashSet<>();
//            Role role = roleRepository.findByRole(RoleConstant.ROLE_ADMIN);
//            roles.add(role);
//            userInfo.setRoles(roles);
//            userInfoRepository.saveAndFlush(userInfo);
//            UserRoleCollegeMapping userRoleCollegeMapping = new UserRoleCollegeMapping();
//            userRoleCollegeMapping.setRole(role);
//            userRoleCollegeMapping.setUser(userInfo);
//            userRoleCollegeRepo.saveAndFlush(userRoleCollegeMapping);
//            System.out.println(" ****  Admin Created  ****");
//        }
//
//
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.cors().and()
//                // starts authorizing configurations
//                .authorizeRequests()
//                // ignoring the guest's urls "
//                .antMatchers("/account/login","/logout","/college/collegeDropDown").permitAll()
//                // authenticate all remaining URLS
//                .anyRequest().authenticated().and()
//                /* "/logout" will log the user out by invalidating the HTTP Session,
//                 * cleaning up any {link rememberMe()} authentication that was configured, */
//                .logout()
//                .permitAll()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
//                .logoutUrl("/account/logout")
//                .logoutSuccessUrl("/account/login")
//                .and()
//                // enabling the basic authentication
//                .httpBasic().and()
//                // configuring the session on the server
//                // disabling the CSRF - Cross Site Request Forgery
//                .csrf().disable();
//    }
//
//
//}
