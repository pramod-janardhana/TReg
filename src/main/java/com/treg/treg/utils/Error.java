package com.treg.treg.utils;

public class Error {
    private final String errMsg;
    private final int statusCode;

    public Error(String errMsg, int statusCode ) {
        this.errMsg = errMsg;
        this.statusCode = statusCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
