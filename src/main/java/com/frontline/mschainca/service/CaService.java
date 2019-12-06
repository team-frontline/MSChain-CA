package com.frontline.mschainca.service;


import com.frontline.mschainca.config.CaDetailsConfig;
import com.frontline.mschainca.dto.CertificateDto;
import com.frontline.mschainca.dto.ResponseDto;
import com.frontline.mschainca.util.Util;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
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

    public byte[] signCertificate(String certificate) throws InvalidKeySpecException, SignatureException,
            NoSuchAlgorithmException, InvalidKeyException, IOException {
        return Util.signString(certificate);
    }

    public ResponseDto revokeCertificate(String cert) throws InvalidKeySpecException, CertificateException,
            OperatorCreationException, NoSuchAlgorithmException, IOException, SignatureException, InvalidKeyException {
        StringBuilder responseStringBuilder = new StringBuilder();

        String caCert = Util.stringFromCert(Util.generateSelfSingedCert());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cert", cert);
        params.add("caCert", caCert);
        params.add("caSig", new String(Hex.encode(Util.signString(cert))));

        Mono<ClientResponse> responseMono = WebClient.create()
                .post()
                .uri("http://52.45.29.135:3000/api/revoke")
                .body(BodyInserters.fromFormData(params))
                .exchange();
        return responseMono.flatMap(res -> res.bodyToMono(ResponseDto.class)).block();
    }

    public ResponseDto issueCertificate(String cert) throws InvalidKeySpecException, CertificateException,
            OperatorCreationException, NoSuchAlgorithmException, IOException {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cert", cert);
        params.add("intermediateCert", Util.stringFromCert(Util.generateSelfSingedCert()));
        params.add("sig", "");

        Mono<ClientResponse> responseMono = WebClient.create()
                .post()
                .uri("http://52.45.29.135:3000/api/issue")
                .body(BodyInserters.fromFormData(params))
                .exchange();

        return responseMono.flatMap(res -> res.bodyToMono(ResponseDto.class)).block();
    }

    public ResponseDto updateCertificate(String cert, String signature) throws InvalidKeySpecException, CertificateException,
            OperatorCreationException, NoSuchAlgorithmException, IOException {
        if (Util.checkCertificateIssuer(cert, CaDetailsConfig.COMMON_NAME)) {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("cert", cert);
            params.add("intermediateCert", Util.stringFromCert(Util.generateSelfSingedCert()));
            params.add("sig", signature);

            Mono<ClientResponse> responseMono = WebClient.create()
                    .post()
                    .uri("http://52.45.29.135:3000/api/issue")
                    .body(BodyInserters.fromFormData(params))
                    .exchange();
            return responseMono.flatMap(res -> res.bodyToMono(ResponseDto.class)).block();
        } else return new ResponseDto("The proposed certificate is not issued from the CA: " + CaDetailsConfig.CA_NAME);
    }

    public CertificateDto getProposedCertificate(String csr) throws InvalidKeySpecException, OperatorCreationException,
            NoSuchAlgorithmException, IOException {
        CertificateDto certificateDto = new CertificateDto();
        certificateDto.setCertificate(Util.signCSR(Util.getCSRfromString(csr)));
        return certificateDto;
    }

    public String getSignedProposedCertificate(String proposedCert, String msUrl) {
        return null;
    }


}
