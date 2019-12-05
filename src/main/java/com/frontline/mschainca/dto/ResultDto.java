package com.frontline.mschainca.dto;

import java.io.Serializable;

public class ResultDto implements Serializable {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "status: " + status;
    }
}
