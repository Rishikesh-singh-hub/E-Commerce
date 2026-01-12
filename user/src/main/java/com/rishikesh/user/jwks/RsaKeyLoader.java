package com.rishikesh.user.jwks;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class RsaKeyLoader {

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public RsaKeyLoader() throws Exception {
        this.privateKey = loadPrivateKey("keys/private_pkcs8.pem");
        this.publicKey = loadPublicKey("keys/public.pem");
    }

    private RSAPrivateKey loadPrivateKey(String path) throws Exception {
        String key = readPem(path)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) factory.generatePrivate(
                new PKCS8EncodedKeySpec(decoded)
        );
    }

    private RSAPublicKey loadPublicKey(String path) throws Exception {
        String key = readPem(path)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) factory.generatePublic(
                new X509EncodedKeySpec(decoded)
        );
    }

    private String readPem(String path) throws Exception {
        ClassPathResource resource = new ClassPathResource(path);
        return new String(resource.getInputStream().readAllBytes());
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }
}
