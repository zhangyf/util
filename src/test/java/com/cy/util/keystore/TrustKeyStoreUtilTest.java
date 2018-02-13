package com.cy.util.keystore;

import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.FileSystem;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import static com.cy.util.keystore.KeyStoreUtil.loadKeyStore;

public class TrustKeyStoreUtilTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrustKeyStoreUtilTest.class);

    private String testTrustStorePwd = "test123";

    private String testTrustStore = "testTrust.keystore";

    //@Test
    public void testGetEntryCertificates() throws CertificateException, NoSuchAlgorithmException,
            KeyStoreException, IOException, NoSuchProviderException, SignatureException {
        URL url = ConfigurationUtils.locate(FileSystem.getDefaultFileSystem(), null, testTrustStore);
        KeyStore keyStore = loadKeyStore(url.getFile(), testTrustStorePwd);
        TrustKeyStoreManager pool = TrustKeyStoreUtil.createTrustKeyStoreManager(keyStore);
        List<X509Certificate> certificates = pool.getEntryCertificates();
        LOGGER.debug("entry certificates size : {}", certificates.size());
        for (X509Certificate x509Certificate : certificates) {
            LOGGER.debug(formatCertChain(pool.getChain(x509Certificate), keyStore));
        }
    }

    private String formatCertChain(List<X509Certificate> chain, KeyStore keyStore) throws KeyStoreException {
        StringBuilder sb = new StringBuilder("chain : ");
        for (X509Certificate x509Certificate : chain) {
            sb.append("[")
                    .append(keyStore.getCertificateAlias(x509Certificate))
                    .append("]")
                    .append(", ");
        }
        return sb.toString();
    }
}
