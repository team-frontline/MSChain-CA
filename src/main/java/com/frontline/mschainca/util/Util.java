package com.frontline.mschainca.util;

import com.frontline.mschainca.config.CaDetailsConfig;
import com.frontline.mschainca.config.Config;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509ExtensionUtils;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class Util {

    private static PrivateKey caPrivateKey;
    private static PublicKey caPublicKey;
    private static KeyPair keyPair;
    private static X509Certificate caCertificate;

    public static void decodeCSR(String csr) {
        PemObject pemObject;
        final PemReader pemReader = new PemReader(new StringReader(csr));
        try {
            pemObject = pemReader.readPemObject();
            PKCS10CertificationRequest decodedCsr = new PKCS10CertificationRequest(pemObject.getContent());
            RDN[] rdNs = decodedCsr.getSubject().getRDNs();
            for (RDN rdn : rdNs) {
                String type = (BCStyle.INSTANCE.oidToDisplayName(rdn.getFirst().getType()) != null) ?
                        BCStyle.INSTANCE.oidToDisplayName(rdn.getFirst().getType()) :
                        RFC4519Style.INSTANCE.oidToDisplayName(rdn.getFirst().getType());
                System.out.println(type + " " + IETFUtils.valueToString(rdn.getFirst().getValue()));
            }
            pemReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decodePEM(String cert) throws IOException, CertificateException {
        PemObject pemObject;
        final PemReader pemReader = new PemReader(new StringReader(cert));
        pemObject = pemReader.readPemObject();
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
        ByteArrayInputStream in = new ByteArrayInputStream(pemObject.getContent());
        X509Certificate result = (X509Certificate) certificateFactory.generateCertificate(in);
    }

    public static PKCS10CertificationRequest getCSRfromString(String csrString) {
        PemObject pemObject;
        final PemReader pemReader = new PemReader(new StringReader(csrString));
        PKCS10CertificationRequest csr = null;

        try {
            pemObject = pemReader.readPemObject();
            csr = new PKCS10CertificationRequest(pemObject.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csr;
    }


    public static X509Certificate generateSelfSingedCert() throws NoSuchAlgorithmException, IOException,
            InvalidKeySpecException, OperatorCreationException, CertificateException {
        if (caCertificate == null) {
            File caCertFile = new File(Config.KEY_STORE_PATH + File.separator + Config.CA_CERT_FILE_NAME);
            if (caCertFile.exists()) {
                PemReader pemReader = new PemReader(new FileReader(caCertFile));
                PemObject pemObject = pemReader.readPemObject();
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
                ByteArrayInputStream in = new ByteArrayInputStream(pemObject.getContent());
                caCertificate = (X509Certificate) certificateFactory.generateCertificate(in);
            } else {

                KeyPair keyPair = getKeyPairFromKeyFile(Config.KEY_STORE_PATH + File.separator
                        + Config.PRIVATE_KEY_FILE_NAME);

                final Instant now = Instant.now();
                final Date notBefore = Date.from(now);
                final Date notAfter = Date.from(now.plus(Duration.ofDays(365 * 2)));
                ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WITHRSAENCRYPTION")
                        .build(keyPair.getPrivate());
                X500Name name = new X500Name(BCStyle.INSTANCE, "CN=" + CaDetailsConfig.CA_NAME + ", O="
                        + CaDetailsConfig.ORGANIZATION);
                final X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(
                        name,
                        BigInteger.valueOf(now.toEpochMilli()),
                        notBefore,
                        notAfter,
                        name,
                        keyPair.getPublic())
                        .addExtension(Extension.subjectKeyIdentifier, false, createSubjectKeyId(keyPair.getPublic()))
                        .addExtension(Extension.authorityKeyIdentifier, false, createAuthorityKeyId(keyPair.getPublic()))
                        .addExtension(Extension.basicConstraints, true, new BasicConstraints(true));

                X509CertificateHolder certificateHolder = certificateBuilder.build(contentSigner);

                PemWriter pemWriter = new PemWriter(new FileWriter(Config.KEY_STORE_PATH + File.separator
                        + "certCA1.pem"));
                pemWriter.writeObject(new PemObject("CERTIFICATE", certificateHolder.toASN1Structure().getEncoded()));
                pemWriter.close();
                caCertificate = new JcaX509CertificateConverter().setProvider(new BouncyCastleProvider())
                        .getCertificate(certificateHolder);
            }
        }
        return caCertificate;
    }

    private static SubjectKeyIdentifier createSubjectKeyId(final PublicKey publicKey) throws OperatorCreationException {
        final SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
        final DigestCalculator digCalc =
                new BcDigestCalculatorProvider().get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1));

        return new X509ExtensionUtils(digCalc).createSubjectKeyIdentifier(publicKeyInfo);
    }

    private static AuthorityKeyIdentifier createAuthorityKeyId(final PublicKey publicKey)
            throws OperatorCreationException {
        final SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
        final DigestCalculator digCalc =
                new BcDigestCalculatorProvider().get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1));

        return new X509ExtensionUtils(digCalc).createAuthorityKeyIdentifier(publicKeyInfo);
    }

    public static String stringFromCert(X509Certificate certificate) throws IOException, CertificateEncodingException {
        StringWriter certStringWriter = new StringWriter();
        PemWriter pemWriter = new PemWriter(certStringWriter);
        pemWriter.writeObject(new PemObject("CERTIFICATE", certificate.getEncoded()));
        pemWriter.close();
        return certStringWriter.toString();
    }

    public static String signCSR(PKCS10CertificationRequest csr) throws NoSuchAlgorithmException, IOException,
            InvalidKeySpecException, OperatorCreationException {
        KeyPair keyPair = getKeyPairFromKeyFile(Config.KEY_STORE_PATH + File.separator
                + Config.PRIVATE_KEY_FILE_NAME);
        SubjectPublicKeyInfo caPublicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
        X509v1CertificateBuilder clientCertBuilder = new X509v1CertificateBuilder(
                new X500Name("CN=" + CaDetailsConfig.CA_NAME + ", O=" + CaDetailsConfig.ORGANIZATION),
                BigInteger.valueOf(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L),
                new Date(System.currentTimeMillis() + 2 * 365 * 24 * 60 * 60 * 1000L),
                csr.getSubject(),
                csr.getSubjectPublicKeyInfo()
        );
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
        ContentSigner signer = csBuilder.build(keyPair.getPrivate());
        X509CertificateHolder certificateHolder = clientCertBuilder.build(signer);

        StringWriter stringWriter = new StringWriter();
        PemWriter pemWriter = new PemWriter(stringWriter);
        pemWriter.writeObject(new PemObject("CERTIFICATE", certificateHolder.toASN1Structure().getEncoded()));
        pemWriter.close();

        return stringWriter.toString();
    }

    public static byte[] signString(String message) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException,
            InvalidKeyException, SignatureException {
        Security.addProvider(new BouncyCastleProvider());
        KeyPair keyPair = getKeyPairFromKeyFile(Config.KEY_STORE_PATH + File.separator
                + Config.PRIVATE_KEY_FILE_NAME);
        PrivateKey privateKey = keyPair.getPrivate();
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes());
        return signature.sign();
    }

    public static boolean verifySignedString(String message, byte[] signedMessage) throws NoSuchAlgorithmException,
            IOException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        Security.addProvider(new BouncyCastleProvider());
        PublicKey publicKey = getKeyPairFromKeyFile(Config.KEY_STORE_PATH + File.separator
                + Config.PRIVATE_KEY_FILE_NAME).getPublic();
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(message.getBytes());
        return signature.verify(signedMessage);
    }

    public static PublicKey getPublicKeyFromFile(String filename)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (caPublicKey == null) {
            PemReader pemReader = new PemReader(new FileReader(filename));
            PemObject pemObject = pemReader.readPemObject();
            byte[] keyBytes = pemObject.getContent();
            pemReader.close();
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            caPublicKey = kf.generatePublic(spec);
        }
        return caPublicKey;
    }

    public static KeyPair getKeyPairFromKeyFile(String filePath) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        if (keyPair == null) {
            PemReader pemReader = new PemReader(new FileReader(filePath));
            PemObject pemObject = pemReader.readPemObject();
            byte[] content = pemObject.getContent();

            PrivateKey privateKey = null;
            PublicKey publicKey;

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            if (pemObject.getType().equals("RSA PRIVATE KEY")) {
                ASN1Sequence sequence = ASN1Sequence.getInstance(content);
                RSAPrivateKey key = RSAPrivateKey.getInstance(sequence);

                RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
                RSAPrivateCrtKeySpec privateCrtKeySpec = new RSAPrivateCrtKeySpec(
                        key.getModulus(),
                        key.getPublicExponent(),
                        key.getPrivateExponent(),
                        key.getPrime1(),
                        key.getPrime2(),
                        key.getExponent1(),
                        key.getExponent2(),
                        key.getCoefficient()
                );
                publicKey = keyFactory.generatePublic(publicKeySpec);
                privateKey = keyFactory.generatePrivate(privateCrtKeySpec);
            } else if (pemObject.getType().equals("PUBLIC KEY")) {
                KeySpec keySpec = new X509EncodedKeySpec(content);
                publicKey = keyFactory.generatePublic(keySpec);
            } else if (pemObject.getType().equals("RSA PUBLIC KEY")) {
                ASN1Sequence sequence = ASN1Sequence.getInstance(content);
                RSAPublicKey key = RSAPublicKey.getInstance(sequence);
                RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
                publicKey = keyFactory.generatePublic(publicKeySpec);
            } else {
                throw new IllegalArgumentException(pemObject.getType() + "is not supported");
            }
            keyPair = new KeyPair(publicKey, privateKey);
        }
        return keyPair;
    }

    public static MultiValueMap<String, String> getResponseHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("name", CaDetailsConfig.COMMON_NAME);
        return headers;
    }
}
