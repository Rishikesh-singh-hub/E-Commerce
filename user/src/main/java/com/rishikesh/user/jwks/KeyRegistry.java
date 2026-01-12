package com.rishikesh.user.jwks;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KeyRegistry {

    private final Map<String, RSAKeyPair> keys = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() throws Exception {
        generateAndStore("user-key-2026-01");
    }

    private void generateAndStore(String kid) throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();

        keys.put(kid, new RSAKeyPair(
                (RSAPrivateKey) keyPair.getPrivate(),
                (RSAPublicKey) keyPair.getPublic(),
                kid
        ));
    }

    public RSAKeyPair getByKid(String kid) {
        return keys.get(kid);
    }

    public Collection<RSAKeyPair> all() {
        return keys.values();
    }
}
