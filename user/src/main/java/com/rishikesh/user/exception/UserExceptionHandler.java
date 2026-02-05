package com.rishikesh.user.exception;

import com.rishikesh.contracts.exception.EmailAlreadyExistException;
import com.rishikesh.contracts.exception.ErrorResponse;
import com.rishikesh.contracts.exception.PhoneNumberExist;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> emailAlreadyExistException(EmailAlreadyExistException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .code(400)
                        .message(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> duplicateKey(DuplicateKeyException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .code(400)
                        .message(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(PhoneNumberExist.class)
    public ResponseEntity<ErrorResponse> phoneNumberExist(PhoneNumberExist ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .code(400)
                        .message(ex.getMessage())
                        .build()
                );
    }



}
