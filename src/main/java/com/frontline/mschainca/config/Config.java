package com.frontline.mschainca.config;

public class Config {

    public static final String KEY_STORE_PATH = "E:\\FYP\\Implementations\\mschain-ca\\resources\\keystore";

    /*  To genetare private key
            $ openssl genpkey -out rsakey.pem -algorithm RSA -pkeyopt rsa_keygen_bits:2048
        To extrect public key from private key
            $ openssl rsa -in private_key.pem -pubout -out public_key.pem
    * */

    public static final String PRIVATE_KEY_FILE_NAME = "private_key.pem";

    public static final String PUBLIC_KEY_FILE_NAME = "public_key.pem";

    public static final String CA_NAME = "CA1";
}
