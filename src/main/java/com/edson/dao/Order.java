package com.edson.dao;

public enum Order {

    ASC, DESC;

    public boolean isAscOrder() {
        return ASC.equals(this);
    }

    public boolean isDescOrder() {
        return DESC.equals(this);
    }
}