package com.frontline.mschainca.dto;

import java.io.Serializable;

public class CertificateDto implements Serializable {

    private String certificate;

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }
}
