package com.cy.util.keystore;

import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.FileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class KeyKeyStoreUtil {

    public static Certificate[] getKeyCertificateChain(KeyStore keyStore) throws KeyStoreException {
        Certificate keyCertificate = getKeyCertificate(keyStore);
        if (keyCertificate != null) {
            String alias = keyStore.getCertificateAlias(keyCertificate);
            return keyStore.getCertificateChain(alias);
        } else {
            return null;
        }
    }

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

    public static Certificate getKeyCertificate(KeyStore keyStore) throws KeyStoreException {
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
            if (keyStore.isKeyEntry(alias)) {
                return certificate;
            }
        }
        return null;
    }
}
