package com.frontline.mschainca.service;


import com.frontline.mschainca.util.Util;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;

@Service
public class CaService {


    public String[] decodeCSR(String csr) {
        Util.decodeCSR(csr);
        return null;
    }

    public String signCSR(PKCS10CertificationRequest csr){
        
        return null;
    }


}
