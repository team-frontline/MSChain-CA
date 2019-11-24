package com.frontline.mschainca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
        return null;
    }

    public void revokeCertificate() {
    }


}
