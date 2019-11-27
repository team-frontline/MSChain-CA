package com.frontline.mschainca.controller;

import com.frontline.mschainca.util.Util;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Controller
@RequestMapping(value = "ca/")
public class CaController {

    /*
    let issueBody = {
        "cert": "",
        "intermediateCert": "",
        "sig": ""
    };
    let revokeBody = {
        "cert": "",
        "caCert": "",
        "caSig": ""
    };
    * */

    public String requestNewCertificate(@RequestBody String csr) {
        try {
            String proposedCert = Util.signCSR(Util.getCSRfromString(csr));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | OperatorCreationException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void revokeCertificate() {
    }


}
