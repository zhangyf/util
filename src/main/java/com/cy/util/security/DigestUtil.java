package com.cy.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtil {
    private static final String SHA1 = "SHA1";

    public static byte[] digetstSHA1(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        digest.update(data);
        return digest.digest();
    }
}
