package com.neo4j_ecom.demo.utils.enums;

public enum SellingType {
    STORE("In-store selling only"),
    ONLINE("online selling only"),
    BOTH("Available in-store and online");

    private final String type;

    SellingType(String type) {
        this.type = type;
    }
}