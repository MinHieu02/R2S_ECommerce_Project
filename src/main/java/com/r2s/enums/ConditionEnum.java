package com.r2s.enums;

public enum ConditionEnum {
    NEW, OLD, REFURBISHED;
    public static boolean contains(String value) {
        for (ConditionEnum condition : ConditionEnum.values()) {
            if (condition.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}

