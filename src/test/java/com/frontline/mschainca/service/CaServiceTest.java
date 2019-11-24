package com.frontline.mschainca.service;

import com.frontline.mschainca.util.Util;
import org.bouncycastle.operator.OperatorCreationException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.*;


class CaServiceTest {
    @InjectMocks
    CaService caService;

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

    @Test
    void decodeCSR() {
    }

    @Test
    void testDecodeCSR() {
    }

    @Test
    void signCSR() {
    }

    @Test
    void signCertificate() {
    }

//    @Test
//    void issueCertificate() {
//        try {
//            caService.issueCertificate(Util.signCSR(Util.getCSRfromString(csr)));
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (OperatorCreationException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Assert.assertTrue(true);
//    }
}