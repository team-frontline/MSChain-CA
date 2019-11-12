package com.frontline.mschainca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class CaController {

    public String requestNewCertificate(@RequestBody String csr){
        return null;
    }

    public void revokeCertificate(){}


}
