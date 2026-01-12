//package com.rishikesh.user.jwt;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.security.KeyFactory;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
//
//@Component
//public class RsaKeyProvider {
//
//    public RSAPrivateKey privateKey;
//    public RSAPublicKey publicKey;
//
//    public RsaKeyProvider(
//            @Value("${spring.app.privateKey}") String privateKeyBase64,
//            @Value("${spring.app.publicKey}") String publicKeyBase64
//    ) throws Exception {
//
//        this.privateKey = loadPrivateKey(privateKeyBase64);
//        this.publicKey = loadPublicKey(publicKeyBase64);
//    }
//
//    private RSAPrivateKey loadPrivateKey(String key) throws Exception {
//        byte[] decoded = Base64.getDecoder().decode(key);
//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
//        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
//    }
//
//    private RSAPublicKey loadPublicKey(String key) throws Exception {
//        byte[] decoded = Base64.getDecoder().decode(key);
//        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
//        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
//    }
//
//    public RSAPrivateKey privateKey() {
//        return privateKey;
//    }
//
//    public RSAPublicKey publicKey() {
//        return publicKey;
//    }
//}
