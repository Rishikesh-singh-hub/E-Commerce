package com.rishikesh.user.jwks;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@AllArgsConstructor
@Getter
public class RSAKeyPair {

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;
    private String kid;



}
