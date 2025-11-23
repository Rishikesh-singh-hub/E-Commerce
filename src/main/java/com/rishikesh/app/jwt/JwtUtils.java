package com.rishikesh.user.jwt;

import com.rishikesh.user.configuration.UserDetailsConfig;
import com.rishikesh.user.entity.UserEntity;
import com.rishikesh.user.repository.UserRepo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtils {

    @Value("${spring.app.jwtSecret}")
    private String jwtKey;
    private final UserRepo userRepo;
    private final UserDetailsService userDetailsService;

    public JwtUtils(UserRepo userRepo, UserDetailsService userDetailsService) {
        this.userRepo = userRepo;
        this.userDetailsService = userDetailsService;
    }

    public String getJwtFromHeader(HttpServletRequest request){

        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")){
            String jwt = token.substring(7);
            if(validateJwt(jwt))
                return jwt;
        }
        return null;
    }

    public boolean validateJwt(String jwt)
            throws MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {

        Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(jwt);
        return true;

    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtKey));
    }

    public Optional<UserEntity> getIdFromJwt(String jwt){

        Claims claim = Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        return userRepo.findByEmail(claim.getSubject());

    }

    public String generateJwt(UserEntity entity) {

        Date date = new Date();
        return Jwts.builder()
                .subject(entity.getId())
                .claim("roles",entity.getRole())
                .issuedAt(date)
                .expiration(new Date(System.currentTimeMillis()+2000*60*60))
                .signWith((SecretKey) key())
                .compact();

    }
    public void authenticationManager(AuthenticationManagerBuilder auth)throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(UserDetailsConfig.passwordEncoder());
    }
}
