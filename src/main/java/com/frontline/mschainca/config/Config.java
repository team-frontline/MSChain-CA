package com.frontline.mschainca.config;

import java.io.File;

public class Config {

    private static final String BASE_PATH = new File("").getAbsolutePath();

    public static final String KEY_STORE_PATH = BASE_PATH + File.separator+ "src"+ File.separator + "main" + File.separator + "resources" + File.separator + "keystore";

    /*  To genetare private key
            $ openssl genpkey -out rsakey.pem -algorithm RSA -pkeyopt rsa_keygen_bits:2048
        To extrect public key from private key
            $ openssl rsa -in private_key.pem -pubout -out public_key.pem
    * */

    public static final String PRIVATE_KEY_FILE_NAME = "CA1.key";

    public static final String PUBLIC_KEY_FILE_NAME = "public_key.pem";

}
