package com.frontline.mschainca.dto;

import java.io.Serializable;

public class ErrorDto implements Serializable {

    private String message;

    private String stack;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }
}
