package com.cy.util.keystore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.*;

public class KeyStoreCertificateChainPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyStoreCertificateChainPool.class);

    private KeyStore keyStore;

    Map<Principal, CertificateNode> map = new HashMap<Principal, CertificateNode>();

    private KeyStoreCertificateChainPool(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    public boolean isEntryCertificate(String alias) throws KeyStoreException {
        X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
        return isEntryCertificate(certificate);
    }

    public boolean isEntryCertificate(X509Certificate certificate) {
        if (certificate == null) {
            return false;
        }
        return isEntryCertificate(certificate.getSubjectDN());
    }

    public boolean isEntryCertificate(Principal subjectDN) {
        if (subjectDN == null) {
            return false;
        }
        CertificateNode node = map.get(subjectDN);
        if (node != null && node.getChildCertNum() == 0) {
            return true;
        }
        return false;
    }

    public List<X509Certificate> getChain(String alias) throws KeyStoreException {
        X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(alias);
        if (x509Certificate == null) {
            throw new NullPointerException("invalid alias : " + alias);
        }
        return getChain(x509Certificate);
    }

    public List<X509Certificate> getChain(X509Certificate entryCertificate) {
        if (entryCertificate == null) {
            throw new NullPointerException("empty entry certificate");
        }
        Principal subjectDN = entryCertificate.getSubjectDN();
        return getChain(subjectDN);
    }

    public List<X509Certificate> getChain(Principal subjectDN) {
        if (subjectDN == null) {
            throw new NullPointerException("empty subjectDN");
        }
        List<X509Certificate> certificateList = new ArrayList<X509Certificate>();
        CertificateNode certificateNode = map.get(subjectDN);
        if (isEntryCertificate(certificateNode.getCertificate())) {
            while (certificateNode != null && certificateNode.getCertificate() != null) {
                certificateList.add(certificateNode.getCertificate());
                certificateNode = map.get(map.get(certificateNode.getIssuerDN()));
            }
        }
        return certificateList;
    }

    public List<X509Certificate> getEntryCertificate() {
        Set<Map.Entry<Principal, CertificateNode>> entrySet = map.entrySet();
        Iterator<Map.Entry<Principal, CertificateNode>> iterator = entrySet.iterator();
        List<X509Certificate> certificateList = new ArrayList<X509Certificate>();
        while (iterator.hasNext()) {
            Map.Entry<Principal, CertificateNode> entry = iterator.next();
            CertificateNode certificateNode = entry.getValue();
            if (isEntryCertificate(certificateNode.getCertificate())) {
                certificateList.add(certificateNode.getCertificate());
            }
        }
        return certificateList;
    }

    private CertificateNode addCertificate(X509Certificate x509Certificate) {
        Principal issuerDN = x509Certificate.getIssuerDN();
        Principal subjectDN = x509Certificate.getSubjectDN();

        CertificateNode issuerNode = null;
        if (map.containsKey(issuerDN)) {
            issuerNode = map.get(issuerDN);
        } else {
            issuerNode = new CertificateNode();
            map.put(issuerDN, issuerNode);
        }
        if (!x509Certificate.getIssuerDN().equals(x509Certificate.getSubjectDN())) {
            issuerNode.setChildCertNum(issuerNode.getChildCertNum() + 1);
        } else {
            LOGGER.info("self-signed certificate : {}", x509Certificate.getSubjectDN());
        }

        CertificateNode subjectNode = null;
        if (map.containsKey(subjectDN)) {
            subjectNode = map.get(subjectDN);
        } else {
            subjectNode = new CertificateNode();
            map.put(subjectDN, subjectNode);
        }
        if (subjectNode.getCertificate() != null) {
            LOGGER.warn("certificate re-set, old subjectDN : {}, new subjectDN: {}",
                    subjectNode.getCertificate().getSubjectDN(),
                    x509Certificate.getSubjectDN());
        }
        if (subjectNode.getIssuerDN() != null) {
            LOGGER.warn("issuerDN re-set, old issuerDN : {}, new issuerDN : {}",
                    subjectNode.getIssuerDN(),
                    x509Certificate.getIssuerDN());
        }
        subjectNode.setCertificate(x509Certificate);
        subjectNode.setIssuerDN(x509Certificate.getIssuerDN());

        return subjectNode;
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public static KeyStoreCertificateChainPool createKeyStoreCertificateChainPool(KeyStore keyStore)
            throws KeyStoreException {
        KeyStoreCertificateChainPool keyStoreCertificateChainPool = new KeyStoreCertificateChainPool(keyStore);
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
            keyStoreCertificateChainPool.addCertificate(certificate);
        }
        return keyStoreCertificateChainPool;
    }

    public static class CertificateNode {
        private int childCertNum;

        private X509Certificate certificate;

        private Principal issuerDN;

        public int getChildCertNum() {
            return childCertNum;
        }

        public void setChildCertNum(int childCertNum) {
            this.childCertNum = childCertNum;
        }

        public X509Certificate getCertificate() {
            return certificate;
        }

        public void setCertificate(X509Certificate certificate) {
            this.certificate = certificate;
        }

        public Principal getIssuerDN() {
            return issuerDN;
        }

        public void setIssuerDN(Principal issuerDN) {
            this.issuerDN = issuerDN;
        }
    }
}
