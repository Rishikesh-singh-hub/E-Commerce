package com.rishikesh.app.jwt;

import com.rishikesh.app.configuration.UserDetailsConfig;
import com.rishikesh.app.entity.UserEntity;
import com.rishikesh.app.repository.UserRepo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${spring.app.jwtSecret}")
    private String jwtKey;
    private final UserRepo userRepo;
    private final UserDetailsService userDetailsService;

    public JwtUtils(UserRepo userRepo, UserDetailsService userDetailsService) {
        this.userRepo = userRepo;
        this.userDetailsService = userDetailsService;
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
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
        logger.info("id found : {}",claim.getSubject());
        return userRepo.findById(claim.getSubject());

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

    public boolean isTokenValidForUser(String token, UserEntity user) {
        // 1) Parse claims and validate signature/structure. Let parsing exceptions bubble up:
        //    ExpiredJwtException, MalformedJwtException, SignatureException, UnsupportedJwtException, IllegalArgumentException
        Claims claims = parseClaims(token); // MUST throw on invalid token

        // 2) Subject check (critical)
        String subject = claims.getSubject();
        if (subject == null) return false;
        // If your sub contains id instead of email, adapt this check accordingly.
        if (!subject.equalsIgnoreCase(user.getId())) {
            return false;
        }

        Date now = new Date();


        // 4) Expiry - parseClaims may already have thrown ExpiredJwtException, but double-check defensively
        Date exp = claims.getExpiration();
        if (exp != null && exp.before(now)) {
            return false;
        }

        // 8) User account state
        if (!user.isActive()) {
            return false;
        }

        return true;
    }
}
