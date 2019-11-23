package com.frontline.mschainca.util;

import com.frontline.mschainca.config.Config;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Date;

public class Util {

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
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static X509Certificate generateSelfSingedCert() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException,
            OperatorCreationException, CertificateException {
        KeyPair keyPair = getKeyPairFromFiles(Config.KEY_STORE_PATH
                        + File.separator + Config.PRIVATE_KEY_FILE_NAME,
                Config.KEY_STORE_PATH + File.separator + Config.PUBLIC_KEY_FILE_NAME);
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
        X509v3CertificateBuilder certificateBuilder = new X509v3CertificateBuilder(
                new X500Name("CN=" + Config.CA_NAME),
                BigInteger.valueOf(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L),
                new Date(System.currentTimeMillis() + 2 * 365 * 24 * 60 * 60 * 1000L),
                new X500Name("CN=" + Config.CA_NAME),
                publicKeyInfo
        );
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA512WithRSAEncryption");
        ContentSigner signer = csBuilder.build(keyPair.getPrivate());
        X509CertificateHolder certificateHolder = certificateBuilder.build(signer);

        PemWriter pemWriter = new PemWriter(new FileWriter(Config.KEY_STORE_PATH + File.separator + "certCA1.pem"));
        pemWriter.writeObject(new PemObject("CERTIFICATE", certificateHolder.toASN1Structure().getEncoded()));
        pemWriter.close();

        return new JcaX509CertificateConverter().setProvider(new BouncyCastleProvider())
                .getCertificate(certificateHolder);
    }

    public static String signCSR(PKCS10CertificationRequest csr) throws NoSuchAlgorithmException, IOException,
            InvalidKeySpecException, OperatorCreationException {
        KeyPair keyPair = getKeyPairFromFiles(Config.KEY_STORE_PATH
                        + File.separator + Config.PRIVATE_KEY_FILE_NAME,
                Config.KEY_STORE_PATH + File.separator + Config.PUBLIC_KEY_FILE_NAME);
        SubjectPublicKeyInfo caPublicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
        X509v3CertificateBuilder clientCertBuilder = new X509v3CertificateBuilder(
                new X500Name("CN=" + Config.CA_NAME),
                BigInteger.valueOf(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L),
                new Date(System.currentTimeMillis() + 2 * 365 * 24 * 60 * 60 * 1000L),
                csr.getSubject(),
                csr.getSubjectPublicKeyInfo()
        );
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA512WithRSAEncryption");
        ContentSigner signer = csBuilder.build(keyPair.getPrivate());
        X509CertificateHolder certificateHolder = clientCertBuilder.build(signer);

        StringWriter stringWriter =  new StringWriter();
        PemWriter pemWriter = new PemWriter(stringWriter);
        pemWriter.writeObject(new PemObject("CERTIFICATE", certificateHolder.toASN1Structure().getEncoded()));
        pemWriter.close();

        return stringWriter.toString();
    }

    public static PrivateKey getPrivateKeyFromFile(String filename)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        PemReader pemReader = new PemReader(new FileReader(filename));
        PemObject pemObject = pemReader.readPemObject();
        byte[] keyBytes = pemObject.getContent();
        pemReader.close();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static PublicKey getPublicKeyFromFile(String filename)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        PemReader pemReader = new PemReader(new FileReader(filename));
        PemObject pemObject = pemReader.readPemObject();
        byte[] keyBytes = pemObject.getContent();
        pemReader.close();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static KeyPair getKeyPairFromFiles(String privateKeyFilePath, String publicKeyFilePath)
            throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        PublicKey publicKey = getPublicKeyFromFile(publicKeyFilePath);
        PrivateKey privateKey = getPrivateKeyFromFile(privateKeyFilePath);
        return new KeyPair(publicKey, privateKey);
    }
}
