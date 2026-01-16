package com.rishikesh.product.entity;

public enum SellerStatus {

    PENDING,     // Seller registered, waiting for approval
    ACTIVE,      // Seller approved, can add/manage products
    SUSPENDED    // Seller blocked temporarily or permanently
}
