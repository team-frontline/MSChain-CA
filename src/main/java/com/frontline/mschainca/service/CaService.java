package com.frontline.mschainca.service;


import com.frontline.mschainca.util.Util;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
public class CaService {


    public String[] decodeCSR(String csr) {
        Util.decodeCSR(csr);
        return null;
    }

    public String signCSR(String csrString) throws InvalidKeySpecException, OperatorCreationException,
            NoSuchAlgorithmException, IOException {
        PKCS10CertificationRequest certificationRequest = Util.getCSRfromString(csrString);
        return Util.signCSR(certificationRequest);
    }


}
