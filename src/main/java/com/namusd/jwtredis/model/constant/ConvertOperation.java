package com.namusd.jwtredis.model.constant;

public enum ConvertOperation {
    SPLIT("split"), MERGE("merge");

    private String value;

    ConvertOperation(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
