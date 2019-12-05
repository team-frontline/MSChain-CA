package com.frontline.mschainca.controller;

import com.frontline.mschainca.config.CaDetailsConfig;
import com.frontline.mschainca.dto.CertificateDto;
import com.frontline.mschainca.dto.CsrDto;
import com.frontline.mschainca.dto.ResponseDto;
import com.frontline.mschainca.service.CaService;
import com.frontline.mschainca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("name", CaDetailsConfig.COMMON_NAME);

        return new ResponseEntity<ResponseDto>(responseDto,  headers, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/certificate/revoke")
    public ResponseEntity<ResponseDto> revokeCertificate(@RequestBody CertificateDto certificateToRevoke) {
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

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("name", CaDetailsConfig.COMMON_NAME);

        return new ResponseEntity<ResponseDto>(responseDto,  headers, HttpStatus.OK);
    }
}
