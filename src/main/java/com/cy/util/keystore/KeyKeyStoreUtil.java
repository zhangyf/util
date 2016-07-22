package com.cy.util.keystore;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class KeyKeyStoreUtil extends KeyStoreUtil {

    public static Certificate[] getKeyCertificateChain(KeyStore keyStore) throws KeyStoreException {
        Certificate keyCertificate = getKeyCertificate(keyStore);
        if (keyCertificate != null) {
            String alias = keyStore.getCertificateAlias(keyCertificate);
            return keyStore.getCertificateChain(alias);
        } else {
            return null;
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
