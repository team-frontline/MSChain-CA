package com.frontline.mschainca.config;

import java.io.File;

public class Config {

    public static String TEST_CERT = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDCTCCAfECBgFu1SwJVjANBgkqhkiG9w0BAQsFADAmMRYwFAYDVQQDDA1hZG1p\n" +
            "bi5jYTEuY29tMQwwCgYDVQQKDANDQTEwHhcNMTkxMjA0MDgyNzQzWhcNMjExMjA0\n" +
            "MDgyNzQzWjBqMQswCQYDVQQGEwJTTDENMAsGA1UECBMEV2VzdDEQMA4GA1UEBxMH\n" +
            "Q29sb21ibzEWMBQGA1UEChMNRnJvbnRsaW5lLm9yZzEQMA4GA1UECxMHTVNDaGFp\n" +
            "bjEQMA4GA1UEAxMHbXMxLm9yZzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoC\n" +
            "ggEBALXZxVXZS1uLJwe9d8oPkNqv1Oh2HrMKo23Z7aIXRyWLxDhBJZr7EO9/Xz18\n" +
            "7Dla8T9/5VWFygSqhf92SQHgwLCmy4ul0Ka0tB1Z7l+Zd5WLRSe8kEf3aETXAxqU\n" +
            "bqFZBFNhMkEt4Fv7XeZX+1JYdTzULt4CqPwjAHmMS825mPm+BSsYu4LzsXO1iYId\n" +
            "l4nufJh67QHN8FOuT8bDCneWAyZU6jPelqfbpkhek28BDF5Y6iPg1wGq+L0biOMD\n" +
            "XEeCuqUdw3Fe01TJhoon6C2spOXHtSSt7pDtzKvkBPdA4ISaEZsr6F9gr3U+Ea6C\n" +
            "uqhLTUeezK19c57Cuxt8QvWQJ+cCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAPQzF\n" +
            "V3qJFbAxDhxcF6kDqIrEkQzIf1MuCfGXSdys749Pyfunmj0qGzUB69WcfZ6taF+U\n" +
            "VazXc45z3KOnunqQYYVK6llTr7CI+PJfBQCX8B8fZ1EUcmIpcGauLCc36zRtwkAA\n" +
            "TOudDSPpH5GWZV3W/anYLxI2UBahsf936qoXIW9STNhnD45WUruLEfocM5bdKT7o\n" +
            "AKvoPypdYVAKblq5uxCpFUiQ16nuIykXwYik+Qv9gZIb/d2jVp12ZTdweEnspipt\n" +
            "bKIhfy52UBTC9y+G9Vcthy/awBWCUDME9MD9r6Qif77N19de3deZHspuGlCUem4D\n" +
            "gA5B0Dz/8BBYAv+Y2w==\n" +
            "-----END CERTIFICATE-----\n";

    private static final String BASE_PATH = new File("").getAbsolutePath();

    public static final String KEY_STORE_PATH = BASE_PATH + File.separator + "resources" + File.separator + "keystore";

    /*  To genetare private key
            $ openssl genpkey -out rsakey.pem -algorithm RSA -pkeyopt rsa_keygen_bits:2048
        To extrect public key from private key
            $ openssl rsa -in private_key.pem -pubout -out public_key.pem
    * */

    public static final String PRIVATE_KEY_FILE_NAME = "CA1.key";

    public static final String PUBLIC_KEY_FILE_NAME = "public_key.pem";

    public static final String CA_CERT_FILE_NAME = "certCA1.pem";

    public static boolean DEMO_MODE =  false;

}
