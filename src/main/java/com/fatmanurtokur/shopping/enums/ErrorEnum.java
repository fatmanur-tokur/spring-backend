package com.fatmanurtokur.shopping.enums;

public enum ErrorEnum {
    SUCCESS(200, "Success"),
    FAILED(300, "Failed"),
    INVALID_PASSWORD(401,"Invalid password."),
    USER_NOT_FOUND(404,"User Not found"),
    USER_ALREADY_EXISTS(409,"User Already Exists");


    private final int code;
    private final String msg;
    ErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
