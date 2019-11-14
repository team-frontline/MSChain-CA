package com.frontline.mschainca.util;

import com.frontline.mschainca.config.Config;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

class UtilTest {

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
}