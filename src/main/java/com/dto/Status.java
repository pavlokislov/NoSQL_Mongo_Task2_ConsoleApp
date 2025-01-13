package com.dto;

public enum Status {

    FAILED, SUCCESS;

    public static Status parse(boolean value) {
        return value ? Status.SUCCESS : Status.FAILED;
    }
}
