package com.cy.util.keystore;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class CertificateUtil {
    private static final String CERTIFICATE_FORMAT = "X509";

    public static PublicKey getPublicKeyFromCert(String certificateFile)
            throws IOException, CertificateException {
        X509Certificate certificate = loadCertificate(certificateFile);
        return certificate.getPublicKey();
    }

    public static X509Certificate loadCertificate(String certificateFile)
            throws IOException, CertificateException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(certificateFile);
            CertificateFactory certificateFactory = CertificateFactory.getInstance(CERTIFICATE_FORMAT);
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(in);
            return certificate;
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    public static boolean isSelfSigned(X509Certificate certificate) throws NoSuchAlgorithmException,
            CertificateException, NoSuchProviderException, SignatureException {
        if (certificate != null && isChildAndParent(certificate, certificate)) {
            return true;
        }
        return false;
    }

    public static boolean isSame(X509Certificate certificate1, X509Certificate certificate2) {
        if (certificate1 != null && certificate2 != null) {
            return certificate1.equals(certificate2);
        }
        return false;
    }

    public static boolean isChildAndParent(X509Certificate childCertificate, X509Certificate parentCertificate)
            throws NoSuchProviderException, CertificateException, NoSuchAlgorithmException, SignatureException {
        if (childCertificate != null && parentCertificate != null) {
            try {
                childCertificate.verify(parentCertificate.getPublicKey());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }


}
