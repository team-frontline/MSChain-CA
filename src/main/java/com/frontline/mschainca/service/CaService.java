package com.frontline.mschainca.service;


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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    public String  revokeCertificate(String cert) throws InvalidKeySpecException, CertificateException,
            OperatorCreationException, NoSuchAlgorithmException, IOException, SignatureException, InvalidKeyException {
        StringBuilder responseStringBuilder = new StringBuilder();

        String caCert =  Util.stringFromCert(Util.generateSelfSingedCert());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cert", cert);
        params.add("caCert", caCert);
//        params.add("caSig", new String(Util.signString(cert), StandardCharsets.ISO_8859_1));
        params.add("caSig", new String(Hex.encode(Util.signString(cert))));

//        Flux<String> stringFlux = WebClient.create()
//                .post()
//                .uri("http://18.232.207.225:3000/api/revoke")
//                .body(BodyInserters.fromFormData(params))
//                .retrieve()
//                .bodyToFlux(String.class);

        Mono<ClientResponse> responseMono = WebClient.create()
                .post()
                .uri("http://52.45.29.135:3000/api/revoke")
                .body(BodyInserters.fromFormData(params))
                .exchange();

//        stringFlux.subscribe(s -> System.out.println(s));
        return responseMono.flatMap(res -> res.bodyToMono(String.class)).block();
    }

    public String issueCertificate(String cert) throws InvalidKeySpecException, CertificateException,
            OperatorCreationException, NoSuchAlgorithmException, IOException {
        StringBuilder responseStriongBuilder = new StringBuilder();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cert", cert);
        params.add("intermediateCert", Util.stringFromCert(Util.generateSelfSingedCert()));
        params.add("sig", "");

//        Flux<String> stringFlux = WebClient.create()
////                .post()
////                .uri("http://18.232.207.225:3000/api/issue")
////                .body(BodyInserters.fromFormData(params))
////                .retrieve()
////                .bodyToFlux(String.class);


        Mono<ClientResponse> responseMono = WebClient.create()
                .post()
                .uri("http://52.45.29.135:3000/api/issue")
                .body(BodyInserters.fromFormData(params))
                .exchange();

        return responseMono.flatMap(res -> res.bodyToMono(String.class)).block();
//
//        stringFlux.subscribe(s -> System.out.println(s));
//        System.out.println("response"+ responseStriongBuilder.toString());
//        return responseStriongBuilder.toString();
    }

    public String getSignedProposedCertificate(String proposedCert, String msUrl) {
        return null;

    }


}
