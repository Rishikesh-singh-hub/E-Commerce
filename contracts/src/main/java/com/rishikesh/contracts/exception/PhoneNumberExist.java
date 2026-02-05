package com.rishikesh.contracts.exception;

public class PhoneNumberExist extends RuntimeException {
    public PhoneNumberExist(String message) {
        super(message);
    }
}
