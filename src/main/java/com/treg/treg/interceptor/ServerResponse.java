package com.treg.treg.interceptor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerResponse<T> {
    private boolean success;
    private T data;
    private String errMsg;

    public ServerResponse() {
    }

    public ServerResponse(boolean success, T data, String errMsg) {
        this.success = success;
        this.data = data;
        this.errMsg = errMsg;
    }
}
