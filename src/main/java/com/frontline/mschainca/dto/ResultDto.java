package com.frontline.mschainca.dto;

import java.io.Serializable;

public class ResultDto implements Serializable {

    private String status;

    private ErrorDto error;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ErrorDto getError() {
        return error;
    }

    public void setError(ErrorDto error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "status: " + status;
    }
}
