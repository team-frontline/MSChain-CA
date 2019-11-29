package com.frontline.mschainca.controller;

import com.frontline.mschainca.service.CaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    CaService caService;


    @ResponseBody
    @RequestMapping(value = "/certificate/new")
    public String requestNewCertificate(@RequestBody String csr, HttpServletRequest request) {
        try {
            //String proposedCert = Util.signCSR(Util.getCSRfromString(csr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(request.getRemoteAddr());
        System.out.println(request.getMethod());
        System.out.println(request.getRemotePort());
        System.out.println(request.getLocalPort());
        System.out.println(request.getServerPort());

        return "All is well";
    }

    @ResponseBody
    @RequestMapping(value = "/certificate/revoke")
    public ResponseEntity<String> revokeCertificate(@RequestBody String certificateToRevoke) {
        String responseRevokeCert = null;
        try {
            responseRevokeCert = caService.revokeCertificate(certificateToRevoke);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseRevokeCert != null ? new ResponseEntity<>(responseRevokeCert, HttpStatus.OK)
                : new ResponseEntity<>("Revocation Failed", HttpStatus.OK);
    }
}
