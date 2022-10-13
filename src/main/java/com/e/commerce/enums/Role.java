package com.e.commerce.enums;

public enum Role {
    USER,
    SELLER,
    ADMIN;

    public String getForClaims() {
        return switch (this) {
            case USER -> "userId";
            case SELLER -> "sellerId";
            case ADMIN -> "adminId";
        };
    }

}
