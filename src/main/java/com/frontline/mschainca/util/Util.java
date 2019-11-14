package com.frontline.mschainca.util;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

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

    public static void generateSelfSingedCert() {
        KeyPair keyPair;
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
