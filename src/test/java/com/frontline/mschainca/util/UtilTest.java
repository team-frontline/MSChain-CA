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
import java.security.*;
import java.security.cert.CertificateException;
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

    final String csrHd = "-----BEGIN CERTIFICATE REQUEST-----\n" +
            "MIIC7TCCAdUCAQAwgY8xCzAJBgNVBAYTAklOMRIwEAYDVQQIDAlOZXcgRGVsaGkx\n" +
            "EjAQBgNVBAcMCU5ldyBEZWxoaTEQMA4GA1UECgwHaGR3b3JrczEOMAwGA1UECwwF\n" +
            "YWRtaW4xFDASBgNVBAMMC2hkd29ya3Mub3JnMSAwHgYJKoZIhvcNAQkBFhFhZG1p\n" +
            "bkBoZHdvcmtzLm9yZzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALlk\n" +
            "C2Vxt9mkwzPCBFZtFTBwnCnxgmg0rZGo27uBGLCtcfQ112JQuDBrSguZKOwH6yjQ\n" +
            "z48awpBNB4N8f4YznIrgXPUHHp0LE9i+185HZZzyhlgPa4uQ8+AqsNxrfjbMgn2q\n" +
            "ted7ptgQ1jLI1eI29M3EKaw+8VSVr8doZbmEjhZGn//vReebIApVfQYWmbwkXYA7\n" +
            "Aao9re6VqUqx/TqyhJ/nFYoajEf8+Y/IwJ3QOvRivq6h6rg36Mx3v14SiSDeiQ63\n" +
            "JfxdvoihV8ceH0mTMT20oZiptK0i5Q1aE2NxCAU57cMnLIvU7ipGx9BvZlXAPRJp\n" +
            "aN7iyqtFV9MHkKWbKBUCAwEAAaAYMBYGCSqGSIb3DQEJBzEJDAdoZHdvcmtzMA0G\n" +
            "CSqGSIb3DQEBCwUAA4IBAQBiwCeee4aErfEduMqAn9+POlGy1t1Mp6S29ApjsmG1\n" +
            "lTgVUa9LE+uhe+S03Wrhz6XcfPhY8kOPCBqzuVVPSp+Lr9+lHYD2Dw3JzZy2YMvf\n" +
            "LdcDhI4a/lRAEsyFd6annKJP+4MDwPib9HLkORn04BSsiPPrzH2/z4aoX/oKpHH5\n" +
            "c/Y8h4NA806ru2k0KrFjO7sYhqgDlR5vfVoCzWSkhk0qzxZftfoXNV8nk/9f1CLZ\n" +
            "ur7tffIHs8KkNIh7ca9NJkAfm8eB3U7Q5YLH6pk/weH28df8XK04evDox2mcIhfM\n" +
            "liq8ikZU5+a5suTnv1XmE7SR04LcrNJo7HME+K5vTxIQ\n" +
            "-----END CERTIFICATE REQUEST-----";

    final String certificate = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDkDCCAngCCQCEotp19JSUNjANBgkqhkiG9w0BAQsFADCBiTELMAkGA1UEBhMC\n" +
            "SU4xEjAQBgNVBAgMCU5ldyBEZWxoaTESMBAGA1UEBwwJTmV3IERlbGhpMQwwCgYD\n" +
            "VQQKDANDQTExDjAMBgNVBAsMBUFkbWluMRYwFAYDVQQDDA1hZG1pbi5jYTEuY29t\n" +
            "MRwwGgYJKoZIhvcNAQkBFg1hZG1pbkBjYTEuY29tMB4XDTE4MDQxNzA1MzQwN1oX\n" +
            "DTIxMDIwNDA1MzQwN1owgYkxCzAJBgNVBAYTAklOMRIwEAYDVQQIDAlOZXcgRGVs\n" +
            "aGkxEjAQBgNVBAcMCU5ldyBEZWxoaTEMMAoGA1UECgwDQ0ExMQ4wDAYDVQQLDAVB\n" +
            "ZG1pbjEWMBQGA1UEAwwNYWRtaW4uY2ExLmNvbTEcMBoGCSqGSIb3DQEJARYNYWRt\n" +
            "aW5AY2ExLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMpU6+qr\n" +
            "jtzI3H01XJNebTY/ZJizkGig+ttEPyalCPTavPdZ994mCsuwAPfuLfg5UMgBR3lG\n" +
            "nmNVgkjUf2JdRDyjPLKO35PaKVqSjeUFw53O8gTkT5v1fQH+UYOfXYxgqcY2ADnz\n" +
            "EUS/wMhpXjSyOIgPeGJjeG3sXG7HXGYjVtXu/0IpWqmYCbsKRM1y0mpVpAEcj+wN\n" +
            "n5aYriDFMQMgKq4Jd0KXJh6xXnmlJFDOvLQlzmOkil/FRVgrm22zlDy0iQALBJiG\n" +
            "Vb7Ags/0wkcG0oyNIX2FKIMawTmU0ps/fGUzq54B2Vwf5f9uHQ/xyeyizZ3O/w9P\n" +
            "/crqRgEvPQu5mhkCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAhlSbRnHrydUqed2g\n" +
            "4g2951Scil6e83NN+UQE0ErXb95TOxRIEzZ3rAj1s0/yPhhPsXDWw+72jDms5fnr\n" +
            "LzjC9nomwei/Lu5pIBFKaw/p2YJc8IUAK81vG58NZbhsCN8F2hRf8oZOFYW7E0Yz\n" +
            "JvkGqfz9jTxLqwXCh1rjXzZkgdLHKcrCgAvqf+cTjOLw8kwiNmL92JU8ut++4pug\n" +
            "et4ZNa2OR22Df6EU94AxkvsgF4xWl0wYb6vS+yU4FYmNkfavvbNdPC4XV9NkiSY8\n" +
            "4Dq0htu0KY9qS1d6QnHkGGV2jV2JI7NLidO1D7OEXieuxqSh4iHxz6P776vRvlJ8\n" +
            "sIr9Og==\n" +
            "-----END CERTIFICATE-----\n";

    final String csrAshoka = "-----BEGIN CERTIFICATE REQUEST-----\n" +
            "MIIC+jCCAeICAQAwgZ0xCzAJBgNVBAYTAklOMRIwEAYDVQQIDAlOZXcgRGVsaGkx\n" +
            "EjAQBgNVBAcMCU5ldyBEZWxoaTEaMBgGA1UECgwRQXNob2thIFVuaXZlcnNpdHkx\n" +
            "DjAMBgNVBAsMBUFkbWluMRYwFAYDVQQDDA1hc2hva2EuZWR1LmluMSIwIAYJKoZI\n" +
            "hvcNAQkBFhNhZG1pbkBhc2hva2EuZWR1LmluMIIBIjANBgkqhkiG9w0BAQEFAAOC\n" +
            "AQ8AMIIBCgKCAQEAuFdqbLrtsqYALuF3jk4B4cnNPPsyA/vQXWr5kkBAlfngsyB0\n" +
            "AeI9GOQCPElbqxUKQV0tDOhMcuRJFwguYZCA7mrSy/Akv2p1jmc3s3/AJ9kfUpfb\n" +
            "v6Vl11myLBo92//NF5Aw3I796wXd3wJS8AcW9uwEubOIbRPP5I18bpj9IEnerpBQ\n" +
            "XNIH/634OLlQ69KNz1XR+304Hy+IGoZZeUunm1iTgXAJRPVKG91ADAcQBpEGK+dy\n" +
            "9mOh0LsBgZeX3szctwF2tZh2qZ4ZgBS9oelsebSQFxRgxnHAzelJ1+eslRT+sl6E\n" +
            "QABiWVAhKYJkaazGG94Keynw5nNo5H+4l7vIJQIDAQABoBcwFQYJKoZIhvcNAQkH\n" +
            "MQgMBmFzaG9rYTANBgkqhkiG9w0BAQsFAAOCAQEAXRcuISqyoFPREy5XzGJl/GTN\n" +
            "N1cNJN45ge5wEn1eY3PxFBPl99kZghuUA61wUoo20QWp7nogMQFxf8ZWUYWz58sF\n" +
            "AAzg9rwCm2+W1GbvGsknQKsKeKtVannJVgNc+f+3Lu8WnqPtLafeBKROoAZpB/O2\n" +
            "luv5mlEv+5d3r8US7SMDaeGfRXTYa3WPGYEzVtzjuBnGYFeIjfFxoVVGEv9Xo6QZ\n" +
            "aCPc/5y0KZo5ucLVFfgfM2pbHOIe/Lv6GsQnxLlxgwFH63i3+dgq98OsteI4w0B4\n" +
            "ioKi9iBQiJjVE0OTmj0K4M+AIgeZAW8Oxn/krw7k16qaHr/9QNbdRbkC+psmmg==\n" +
            "-----END CERTIFICATE REQUEST-----\n";

    final String csrSupimi = "-----BEGIN NEW CERTIFICATE REQUEST-----\n" +
            "MIICrzCCAZcCAQAwajELMAkGA1UEBhMCU0wxDTALBgNVBAgTBFdlc3QxEDAOBgNV\n" +
            "BAcTB0NvbG9tYm8xFjAUBgNVBAoTDUZyb250bGluZS5vcmcxEDAOBgNVBAsTB01T\n" +
            "Q2hhaW4xEDAOBgNVBAMTB21zMS5vcmcwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAw\n" +
            "ggEKAoIBAQC12cVV2UtbiycHvXfKD5Dar9Todh6zCqNt2e2iF0cli8Q4QSWa+xDv\n" +
            "f189fOw5WvE/f+VVhcoEqoX/dkkB4MCwpsuLpdCmtLQdWe5fmXeVi0UnvJBH92hE\n" +
            "1wMalG6hWQRTYTJBLeBb+13mV/tSWHU81C7eAqj8IwB5jEvNuZj5vgUrGLuC87Fz\n" +
            "tYmCHZeJ7nyYeu0BzfBTrk/Gwwp3lgMmVOoz3pan26ZIXpNvAQxeWOoj4NcBqvi9\n" +
            "G4jjA1xHgrqlHcNxXtNUyYaKJ+gtrKTlx7Ukre6Q7cyr5AT3QOCEmhGbK+hfYK91\n" +
            "PhGugrqoS01HnsytfXOewrsbfEL1kCfnAgMBAAGgADANBgkqhkiG9w0BAQsFAAOC\n" +
            "AQEAlKIwKl8aZUM7Z8LlHF/Vb0mBYSDwyu3JSUh3TJh2wdkL1m2S61e9GU6an2gb\n" +
            "UslS+Tk+MjumPHoY8h7K2eQNOOKiavrkNru2sVWtQbXUde2bqUh/DA6r+oN1GmnV\n" +
            "ihah8df8qndmffp+z91+aiUyoKAsPAb96gDfEDDm7Go+/kto3rQqp1MATRR2aaeX\n" +
            "9Ro9f7lL2B0ejQJVY5tVxdpaJDWXrnB1Aybh7rmUERmxfI7dQvQSPCFCYc5h2afx\n" +
            "QkeiHPvHCtHloFJCnAyMzES1yqEsGPZAuiCqOn6QVfamnkjGhqL26tAmvUg2Xv0l\n" +
            "3lxIWB0+2AkKt8r52MiWKNZ+CA==\n" +
            "-----END NEW CERTIFICATE REQUEST-----\n";


    @Test
    void decodeCSR() {
    }

    @Test
    void generateSelfSingedCert() {
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
        PKCS10CertificationRequest certificationRequest = Util.getCSRfromString(csrSupimi);
        String certString = null;
        try {
            certString = Util.signCSR(certificationRequest);
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

    @Test
    void signString() throws InvalidKeySpecException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        String message = "hello is it me you looking for";
        byte[] signedMsg = null;
        boolean verified = false;
        try {
            signedMsg = Util.signString(message);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        if (signedMsg != null) {
            verified = Util.verifySignedString(message, signedMsg);
        }
        Assert.assertTrue(verified);
    }

    @Test
    void stringFromCert() {
        X509Certificate certificate = null;
        String certString = null;
        try {
            certificate = Util.generateSelfSingedCert();
            certString = Util.stringFromCert(certificate);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(certString);
    }

    @Test
    void getKeyPairFromKeyFile() {
        KeyPair keyPair = null;
        try {
            keyPair = Util.getKeyPairFromKeyFile(Config.KEY_STORE_PATH + File.separator
                    + Config.PRIVATE_KEY_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(keyPair);
    }

    @Test
    void decodePEM() {
        try {
            Util.decodePEM(certificate);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }
}