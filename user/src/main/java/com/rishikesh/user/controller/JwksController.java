package com.rishikesh.user.controller;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.rishikesh.user.jwks.KeyRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        List<RSAKey> publicKeys = keyRegistry.all().
                entrySet().
                stream().
                map(entry ->
                        new RSAKey.Builder(entry.getValue()).
                                keyID(entry.getKey()).
                                build()
                ).toList();

        return new JWKSet((JWK) publicKeys).toJSONObject();

    }
}
