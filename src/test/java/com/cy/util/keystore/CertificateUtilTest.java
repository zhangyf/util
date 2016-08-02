package com.cy.util.keystore;

import com.cy.util.security.DigestUtil;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;

public class CertificateUtilTest {

    @Test
    public void getSHA1OfCertPk() throws IOException, CertificateException, NoSuchAlgorithmException {
        String certFile = "/home/chenyang/tmp/Snowball.crt";
        PublicKey publicKey = CertificateUtil.getPublicKeyFromCert(certFile);
        byte[] digest = DigestUtil.digetstSHA1(publicKey.getEncoded());
        System.out.println(Hex.encodeHex(digest));
    }
}
