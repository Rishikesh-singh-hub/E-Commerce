package com.rishikesh.contracts.dto.seller;

public enum SellerStatus {

    PENDING,     // Seller registered, waiting for approval
    ACTIVE,      // Seller approved, can add/manage products
    SUSPENDED    // Seller blocked temporarily or permanently
}
