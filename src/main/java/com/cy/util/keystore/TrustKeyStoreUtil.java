package com.cy.util.keystore;

import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class TrustKeyStoreUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrustKeyStoreUtil.class);

    public static KeyStore loadKeyStore(String keyStorePath, String pwd) throws IOException,
            KeyStoreException, CertificateException, NoSuchAlgorithmException {
        URL keyStoreUrl = ConfigurationUtils.locate(FileSystem.getDefaultFileSystem(),
                null, keyStorePath);
        File file = new File(keyStoreUrl.getFile());
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(fileInputStream, pwd.toCharArray());
            return keyStore;
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }

    public static KeyStoreCertificateChainPool parseKeyStore(String keyStorePath, String pwd)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        KeyStore keyStore = loadKeyStore(keyStorePath, pwd);
        return KeyStoreCertificateChainPool.createKeyStoreCertificateChainPool(keyStore);
    }
}
