package com.frontline.mschainca.util;

import com.frontline.mschainca.config.Config;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Optional;

class UtilTest {

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
    void generateSelfSingedCert() {
    }

    @Test
    void getPrivateKeyFromFile() {
        try {
            PrivateKey privateKey = Util.getPrivateKeyFromFile(Config.KEY_STORE_PATH + File.separator +
                    Config.PRIVATE_KEY_FILE_NAME);
            System.out.println(privateKey.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getPublicKeyFromFile() {
        try {
            PublicKey publicKey = Util.getPublicKeyFromFile(Config.KEY_STORE_PATH
                    + File.separator + Config.PUBLIC_KEY_FILE_NAME);
            System.out.println(publicKey.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getKeyPairFromFiles() {
    }

    @Test
    void getCSRfromString() {
        PKCS10CertificationRequest certificationRequest = Util.getCSRfromString(csr);
        RDN[] rdns = certificationRequest.getSubject().getRDNs();
        Optional<RDN> rdnOptional = Arrays.stream(rdns).filter(rdn -> rdn.getFirst().getType().intern() == BCStyle.CN
                || rdn.getFirst().getType().intern() == RFC4519Style.cn).findFirst();
        String cn = rdnOptional.map(rdn -> IETFUtils.valueToString(rdn.getFirst().getValue())).orElse(null);
        Assert.assertEquals("www.google.com", cn);
    }

    @Test
    void testGenerateSelfSingedCert() {
        X509Certificate certificate = null;
        try {
            certificate = Util.generateSelfSingedCert();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(certificate);
    }

    @Test
    void signCSR() {
        PKCS10CertificationRequest certificationRequest = Util.getCSRfromString(csr);
        String certString = null;
        try {
            certString =  Util.signCSR(certificationRequest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(certString);
    }
}