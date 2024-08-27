package com.neo4j_ecom.demo.utils.enums;

public enum SellingType {
    IN_STORE_ONLY("In-store selling only"),
    ONLINE_ONLY("online selling only"),
    IN_STORE_AND_ONLINE("Available in-store and online");

    private final String type;

    SellingType(String type) {
        this.type = type;
    }
}