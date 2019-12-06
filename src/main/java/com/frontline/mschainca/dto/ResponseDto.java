package com.frontline.mschainca.dto;

import java.io.Serializable;

public class ResponseDto implements Serializable {

    private String operation;

    private ResultDto result;

    private String certificate;

    private String message;

    public ResponseDto(){}

    public ResponseDto(String message){
        this.message = message;
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("operation: " + operation)
                .append(", result: [" + result.toString() + "]")
                .append(", certificate: " + certificate)
                .append(", message: " + message);
        return stringBuilder.toString();
    }
}
