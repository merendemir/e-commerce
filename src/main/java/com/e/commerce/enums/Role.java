package com.e.commerce.enums;

public enum Role {
    USER,
    SELLER,
    ADMIN;

    public String getName() {
        return switch (this) {
            case USER -> "user";
            case SELLER -> "seller";
            case ADMIN -> "admin";
        };
    }

}
