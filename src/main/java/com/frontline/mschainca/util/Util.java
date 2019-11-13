package com.frontline.mschainca.util;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.IOException;
import java.io.StringReader;

public class Util {

    public static void decodeCSR(String csr) {
        PemObject pemObject;
        final PemReader pemReader = new PemReader(new StringReader(csr));
        try {
            pemObject = pemReader.readPemObject();
            PKCS10CertificationRequest decodedCsr = new PKCS10CertificationRequest(pemObject.getContent());

            System.out.println(decodedCsr.getSubject().toString());
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
}
