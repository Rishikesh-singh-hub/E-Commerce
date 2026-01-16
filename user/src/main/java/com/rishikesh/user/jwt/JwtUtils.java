package com.rishikesh.user.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.rishikesh.user.entity.UserEntity;
import com.rishikesh.user.jwks.KeyRegistry;
import com.rishikesh.user.jwks.RSAKeyPair;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

    public static final String Active_Kid = "user-key-2026-01";
    public final  KeyRegistry keyRegistry ;

    public JwtUtils(
                    KeyRegistry keyRegistry) {
        this.keyRegistry = keyRegistry;
    }


    private JWSHeader getHeader(RSAKeyPair key){

        return new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(key.getKid())
                .type(JOSEObjectType.JWT)
                .build();
    }

    private JWTClaimsSet getClaims(UserEntity entity) {

            Instant now = Instant.now();

            return new JWTClaimsSet.Builder()
                    .subject(entity.getId())
                    .issuer("user-service")
                    .audience("api-gateway")
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(now.plus(2, ChronoUnit.HOURS)))
                    .claim("roles", List.of(entity.getRole()))
                    .build();

    }



    public String generateJwt(UserEntity entity) throws JOSEException {

        RSAKeyPair key = keyRegistry.getByKid(Active_Kid);
        if (key == null) {
            throw new IllegalStateException("Signing key not found");
        }

        SignedJWT jwt = new SignedJWT(getHeader(key),getClaims(entity));
        JWSSigner signer = new RSASSASigner(key.getPrivateKey());
        jwt.sign(signer);

        return jwt.serialize();

    }

}
