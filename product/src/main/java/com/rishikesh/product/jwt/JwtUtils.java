package com.rishikesh.product.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.List;

@Component
public class JwtUtils {

    @Value("${spring.app.jwtSecret}")
    private  String jwtKey;

    public  Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public  boolean validateJwt(String jwt)
            throws MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {

        Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(jwt);
        return true;

    }

    public  List<String> getRolesFromJwt(String jwt) {

        Claims claims =Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        return (List<String>) claims.get("roles");
    }

    private  Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtKey));
    }

}
