package com.frontline.mschainca.dto;

import java.io.Serializable;

public class ResponseDto implements Serializable {

    private String operation;

    private ResultDto result;

    private String certificate;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public ResultDto getResult() {
        return result;
    }

    public void setResult(ResultDto result) {
        this.result = result;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("operation: " + operation)
                .append(", result: [" + result.toString()+ "]")
                .append(", certificate: " + certificate);
        return stringBuilder.toString();
    }
}
