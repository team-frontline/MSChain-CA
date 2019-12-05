package com.frontline.mschainca.controller;

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
    public ResponseEntity<ResponseDto> requestNewCertificate(@RequestBody CsrDto csrDto) {
        ResponseDto responseDto = null;
        try {
            String proposedCert = Util.signCSR(Util.getCSRfromString(csrDto.getCsr()));
            responseDto = caService.issueCertificate(proposedCert);

            if (responseDto != null && responseDto.getResult().getStatus().equals("OK")) {
                responseDto.setCertificate(proposedCert);
            }
//            response = response.concat(", proposed Cert: " + proposedCert);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/certificate/revoke")
    public ResponseEntity<String> revokeCertificate(@RequestBody CertificateDto certificateToRevoke) {
        String responseRevokeCert = null;
        try {
            responseRevokeCert = caService.revokeCertificate(certificateToRevoke.getCertificate());
        } catch (Exception e) {
            try {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return responseRevokeCert != null ? new ResponseEntity<>(responseRevokeCert, HttpStatus.OK)
                : new ResponseEntity<>("Revocation Failed", HttpStatus.OK);
    }
}
