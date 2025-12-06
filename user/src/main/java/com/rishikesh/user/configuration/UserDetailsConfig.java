package com.rishikesh.user.configuration;

import com.rishikesh.user.entity.UserEntity;
import com.rishikesh.user.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class UserDetailsConfig implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(UserDetailsConfig.class);

    private final UserRepo userRepo;

    public UserDetailsConfig(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Loading user info...");
        UserEntity entity = userRepo.findByEmail(email).orElseThrow();
        logger.info("returning the user data");
        return User.builder()
                .username(entity.getName())
                .password(entity.getPassword())
                .roles(String.valueOf(entity.getRole()))
                .build();
    }



    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}
