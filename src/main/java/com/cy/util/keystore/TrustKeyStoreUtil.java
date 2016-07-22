package com.cy.util.keystore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class TrustKeyStoreUtil extends KeyStoreUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrustKeyStoreUtil.class);

    public static KeyStoreCertificateChainPool parseKeyStore(String keyStorePath, String pwd)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException,
            IOException, NoSuchProviderException, SignatureException {
        KeyStore keyStore = loadKeyStore(keyStorePath, pwd);
        return KeyStoreCertificateChainPool.createKeyStoreCertificateChainPool(keyStore);
    }

    public static KeyStoreCertificateChainPool parseKeyStore(KeyStore keyStore)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException,
            IOException, NoSuchProviderException, SignatureException {
        return KeyStoreCertificateChainPool.createKeyStoreCertificateChainPool(keyStore);
    }

    public static X509Certificate findParentCertificate(X509Certificate childCertificate, KeyStore keyStore)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
            NoSuchProviderException, SignatureException {
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(alias);
            if (CertificateUtil.isChildAndParent(childCertificate, x509Certificate)) {
                return x509Certificate;
            }
        }
        return null;
    }
}
