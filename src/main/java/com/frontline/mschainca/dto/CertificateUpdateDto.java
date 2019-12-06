package com.frontline.mschainca.dto;

import java.io.Serializable;

public class CertificateUpdateDto implements Serializable {

    private String certificate;

    private String signature;

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("certificate: " + certificate)
                .append(", signature : " + signature);
        return stringBuilder.toString();
    }
}
