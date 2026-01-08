package com.rishikesh.cart.exception;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

@RestControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> nullPointer(NullPointerException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .code(400)
                        .message(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<?> handleDuplicateKey(DuplicateKeyException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Email already in use");
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> handleSignature(SignatureException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid JWT signature");
    }

    @ExceptionHandler({
            MalformedJwtException.class,
            ExpiredJwtException.class,
            UnsupportedJwtException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<?> handleJwt(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    @ExceptionHandler (InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorResponse> interAuthServiceEx(InternalAuthenticationServiceException ex){
        return ResponseEntity.badRequest().body(
                ErrorResponse.builder().code(400)
                        .message("Invalid Username and Password")
                        .build()
        );
    }

    @ExceptionHandler( ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<ErrorResponse> changeSetPersister(ChangeSetPersister.NotFoundException ex){
        return  ResponseEntity.badRequest().body(
                ErrorResponse.builder().code(400)
                        .message("Item not found in cart")
                        .build()
        );
    }

}
