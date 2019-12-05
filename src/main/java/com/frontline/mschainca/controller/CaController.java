package com.frontline.mschainca.controller;

import com.frontline.mschainca.config.Config;
import com.frontline.mschainca.dto.CertificateDto;
import com.frontline.mschainca.dto.CsrDto;
import com.frontline.mschainca.dto.ResponseDto;
import com.frontline.mschainca.service.CaService;
import com.frontline.mschainca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static Logger LOGGER = Logger.getLogger(CaController.class.getName());

    @ResponseBody
    @RequestMapping(value = "/certificate/new")
    public ResponseEntity<ResponseDto> requestNewCertificate(@RequestBody CsrDto csrDto) {
        LOGGER.log(Level.INFO, "REQUEST :\n" + csrDto.toString());
        ResponseDto responseDto = null;
        try {
            String proposedCert = null;
            proposedCert = Config.DEMO_MODE ? Config.TEST_CERT : Util.signCSR(Util.getCSRfromString(csrDto.getCsr()));
            responseDto = caService.issueCertificate(proposedCert);
            if (responseDto != null && responseDto.getResult().getStatus().equals("OK")) {
                responseDto.setCertificate(proposedCert);
            } else {
                LOGGER.log(Level.INFO, "Certificate Issue Failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.log(Level.INFO, "RESPONSE :\n" + (responseDto != null ? responseDto.toString() : null));
        return new ResponseEntity<ResponseDto>(responseDto, Util.getResponseHeaders(), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/certificate/revoke")
    public ResponseEntity<ResponseDto> revokeCertificate(@RequestBody CertificateDto certificateToRevoke) {
        LOGGER.log(Level.INFO, "REQUEST :\n" + certificateToRevoke.toString());
        ResponseDto responseDto = null;
        try {
            responseDto = caService.revokeCertificate(certificateToRevoke.getCertificate());
        } catch (Exception e) {
            try {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        LOGGER.log(Level.INFO, "RESPONSE :\n" + (responseDto != null ? responseDto.toString() : null));
        return new ResponseEntity<ResponseDto>(responseDto, Util.getResponseHeaders(), HttpStatus.OK);
    }
}
