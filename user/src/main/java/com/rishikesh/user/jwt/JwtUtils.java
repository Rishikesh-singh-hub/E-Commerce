package com.rishikesh.user.jwt;

import com.rishikesh.user.configuration.UserDetailsConfig;
import com.rishikesh.user.entity.UserEntity;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtils {

    Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private final RsaKeyProvider rsaKeyProvider;
    private final UserDetailsService userDetailsService;

    public JwtUtils(RsaKeyProvider rsaKeyProvider,
                    UserDetailsService userDetailsService) {
        this.rsaKeyProvider = rsaKeyProvider;
        this.userDetailsService = userDetailsService;
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(rsaKeyProvider.publicKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateJwt(String jwt)
            throws MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {

        Jwts.parser()
                .verifyWith(rsaKeyProvider.publicKey())
                .build()
                .parseSignedClaims(jwt);
        return true;

    }

    public String generateJwt(UserEntity entity) {

        Date date = new Date();
        return Jwts.builder()
                .subject(entity.getId())
                .claim("roles",entity.getRole())
                .issuedAt(date)
                .expiration(new Date(System.currentTimeMillis()+2000*60*60))
                .signWith(rsaKeyProvider.privateKey(),Jwts.SIG.RS256)
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
        return user.isActive();
    }
}
