package com.frontline.mschainca;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.StringReader;

@SpringBootApplication
public class MschainCaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MschainCaApplication.class, args);

        /*
        Common Name (CN): www.google.com
		Organization (O): Google Inc
		Organization Unit (OU): Information Technology
		Locality (L): Mountain View
		State (ST): California
		Country (C): US
         */
        final String csr = "-----BEGIN CERTIFICATE REQUEST-----\n" +
                "MIIByjCCATMCAQAwgYkxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlh\n" +
                "MRYwFAYDVQQHEw1Nb3VudGFpbiBWaWV3MRMwEQYDVQQKEwpHb29nbGUgSW5jMR8w\n" +
                "HQYDVQQLExZJbmZvcm1hdGlvbiBUZWNobm9sb2d5MRcwFQYDVQQDEw53d3cuZ29v\n" +
                "Z2xlLmNvbTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEApZtYJCHJ4VpVXHfV\n" +
                "IlstQTlO4qC03hjX+ZkPyvdYd1Q4+qbAeTwXmCUKYHThVRd5aXSqlPzyIBwieMZr\n" +
                "WFlRQddZ1IzXAlVRDWwAo60KecqeAXnnUK+5fXoTI/UgWshre8tJ+x/TMHaQKR/J\n" +
                "cIWPhqaQhsJuzZbvAdGA80BLxdMCAwEAAaAAMA0GCSqGSIb3DQEBBQUAA4GBAIhl\n" +
                "4PvFq+e7ipARgI5ZM+GZx6mpCz44DTo0JkwfRDf+BtrsaC0q68eTf2XhYOsq4fkH\n" +
                "Q0uA0aVog3f5iJxCa3Hp5gxbJQ6zV6kJ0TEsuaaOhEko9sdpCoPOnRBm2i/XRD2D\n" +
                "6iNh8f8z0ShGsFqjDgFHyF3o+lUyj+UC6H1QW7bn\n" +
                "-----END CERTIFICATE REQUEST-----";
        PemObject pemObject;

        final PemReader pemReader = new PemReader(new StringReader(csr));
        try {
            pemObject = pemReader.readPemObject();
            PKCS10CertificationRequest decodedCsr = new PKCS10CertificationRequest(pemObject.getContent());

            /*
            Common Name: www.google.com
			Organization: Google Inc
			Organization Unit: Information Technology
			Locality: Mountain View
			State: California
			Country: US
            * */

            System.out.println(decodedCsr.getSubject().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

