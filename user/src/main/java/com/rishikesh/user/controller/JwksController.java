package com.rishikesh.user.controller;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.rishikesh.user.jwks.KeyRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class JwksController {


    private final KeyRegistry keyRegistry;

    public JwksController(KeyRegistry keyRegistry) {
        this.keyRegistry = keyRegistry;
    }

    @GetMapping("/auth/.well-known/jwks.json")
    public Map<String, Object> jwks() {
        List<JWK> keys = new ArrayList<>();
        keyRegistry.all().forEach((kid,publicKey)->{
            RSAKey key = new RSAKey.Builder(publicKey)
                    .keyID(kid)
                    .build();
            keys.add(key);
        });


        return new JWKSet(keys).toJSONObject();

    }
}
