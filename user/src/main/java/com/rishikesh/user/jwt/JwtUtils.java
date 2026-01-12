package com.rishikesh.user.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.rishikesh.user.entity.UserEntity;
import com.rishikesh.user.jwks.KeyRegistry;
import com.rishikesh.user.jwks.RSAKeyPair;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

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



        Date date = new Date();

        return new JWTClaimsSet.Builder()
                .subject(entity.getId())
                .issuer("user-service")
                .issueTime(date)
                .expirationTime(new Date(System.currentTimeMillis()+2000*60*60))
                .claim("roles",entity.getRole())
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

    public JWTClaimsSet verifyAndParse(String token) throws Exception {

        SignedJWT jwt = SignedJWT.parse(token);

        String kid = jwt.getHeader().getKeyID();
        if (kid == null) {
            throw new SecurityException("Missing kid");
        }

        RSAKeyPair key = keyRegistry.getByKid(kid);
        if (key == null) {
            throw new SecurityException("Unknown kid");
        }

        JWSVerifier verifier = new RSASSAVerifier(key.getPublicKey());

        if (!jwt.verify(verifier)) {
            throw new SecurityException("Invalid signature");
        }

        Date expiry = jwt.getJWTClaimsSet().getExpirationTime();
        if (expiry.before(new Date())) {
            throw new SecurityException("Token expired");
        }

        return jwt.getJWTClaimsSet();
    }



}
