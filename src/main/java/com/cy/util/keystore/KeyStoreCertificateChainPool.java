package com.cy.util.keystore;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import static com.cy.util.keystore.CertificateUtil.*;

public class KeyStoreCertificateChainPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyStoreCertificateChainPool.class);

    private KeyStore keyStore;

    Map<Principal, List<CertificateNode>> map = new HashMap<Principal, List<CertificateNode>>();

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
        List<CertificateNode> nodes = map.get(certificate.getSubjectDN());
        if (CollectionUtils.isNotEmpty(nodes)) {
            for (CertificateNode node : nodes) {
                if (isSame(certificate, node.getCertificate()) &&
                        node.getChildCertificateNodes().size() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<X509Certificate> getChain(String alias) throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
        X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(alias);
        if (x509Certificate == null) {
            throw new NullPointerException("invalid alias : " + alias);
        }
        return getChain(x509Certificate);
    }

    public List<X509Certificate> getChain(X509Certificate entryCertificate) throws CertificateException,
            NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
        if (entryCertificate == null) {
            throw new NullPointerException("empty entry certificate");
        }
        List<X509Certificate> certificateList = new ArrayList<X509Certificate>();
        certificateList.add(entryCertificate);

        X509Certificate childCertificate = entryCertificate;
        List<CertificateNode> nodes = map.get(entryCertificate.getIssuerDN());
        boolean isContinue = true;
        while (CollectionUtils.isNotEmpty(nodes) && isContinue) {
            isContinue = false;
            for (CertificateNode node : nodes) {
                if (node.getCertificate() != null &&
                        isChildAndParent(childCertificate, node.getCertificate()) &&
                        isSame(childCertificate, node.getCertificate())) {
                    certificateList.add(node.getCertificate());
                    childCertificate = node.getCertificate();
                    nodes = map.get(childCertificate.getIssuerDN());
                    isContinue = true;
                    break;
                }
            }
        }
        return certificateList;
    }

    public List<X509Certificate> getEntryCertificates() {
        Set<Map.Entry<Principal, List<CertificateNode>>> entrySet = map.entrySet();
        Iterator<Map.Entry<Principal, List<CertificateNode>>> iterator = entrySet.iterator();
        List<X509Certificate> certificateList = new ArrayList<X509Certificate>();
        while (iterator.hasNext()) {
            Map.Entry<Principal, List<CertificateNode>> entry = iterator.next();
            List<CertificateNode> certificateNodes = entry.getValue();
            for (CertificateNode certificateNode : certificateNodes) {
                if (isEntryCertificate(certificateNode.getCertificate())) {
                    certificateList.add(certificateNode.getCertificate());
                }
            }
        }
        return certificateList;
    }

    private CertificateNode addCertificate(X509Certificate x509Certificate) throws CertificateException,
            NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
        CertificateNode issuerNode = getIssuerNode(x509Certificate);
        CertificateNode subjectNode = getSubjectNode(x509Certificate);
        if (subjectNode.getCertificate() != null || subjectNode.getParentCertificateNode() != null) {
            LOGGER.warn("duplicate adding certificate : {}", x509Certificate.getSubjectDN());
        } else {
            /** 设置父子关系 **/
            subjectNode.setCertificate(x509Certificate);
            subjectNode.setParentCertificateNode(issuerNode);
            if (!isSelfSigned(x509Certificate)) {
                issuerNode.getChildCertificateNodes().add(issuerNode);
            } else {
                // do nothing
            }

            /** 检查加入进去的证书和子证书是不是正在的父子关系 **/
            List<CertificateNode> childCertificateNodes = subjectNode.getChildCertificateNodes();
            List<CertificateNode> notChildCertificateNodes = new LinkedList<CertificateNode>();
            for (CertificateNode certificateNode : childCertificateNodes) {
                if (!isChildAndParent(certificateNode.getCertificate(), x509Certificate)) {
                    notChildCertificateNodes.add(certificateNode);
                    childCertificateNodes.remove(certificateNode);
                }
            }
            /** 如果有不存在真正的父子关系子节点，则新建一个空节点 **/
            if (notChildCertificateNodes.size() > 0) {
                CertificateNode tempSubjectNode = new CertificateNode();
                tempSubjectNode.setChildCertificateNodes(notChildCertificateNodes);
                for (CertificateNode certificateNode : notChildCertificateNodes) {
                    certificateNode.setParentCertificateNode(tempSubjectNode);
                }
                List<CertificateNode> subjectNodes = map.get(x509Certificate.getSubjectDN());
                subjectNodes.add(tempSubjectNode);
            }
        }
        return subjectNode;
    }

    private CertificateNode getIssuerNode(X509Certificate x509Certificate) throws NoSuchAlgorithmException,
            CertificateException, NoSuchProviderException, SignatureException {
        Principal issuerDN = x509Certificate.getIssuerDN();
        CertificateNode issuerNode = null;

        /** 首先找个同issuerDN的node列表 **/
        List<CertificateNode> issuerNodes = null;
        if (map.containsKey(issuerDN)) {
            issuerNodes = map.get(issuerDN);
        } else {
            issuerNodes = new LinkedList<CertificateNode>();
            map.put(issuerDN, issuerNodes);
        }

        /** 找自己的父证书节点 **/
        for (CertificateNode certificateNode : issuerNodes) {
            if (isChildAndParent(x509Certificate, certificateNode.getCertificate())) {
                issuerNode = certificateNode;
                break;
            } else if (certificateNode.getCertificate() == null) {
                /** 一定要保证，如果有cert为空的node，那么这个node一定要在list的最后一个 **/
                issuerNode = certificateNode;
                break;
            } else {
                // do nothing
            }
        }

        /** 如果没有找到自己的父证书节点， 则新建一个 **/
        if (issuerNode == null) {
            issuerNode = new CertificateNode();
            issuerNodes.add(issuerNode);
        }

        return issuerNode;
    }

    private CertificateNode getSubjectNode(X509Certificate x509Certificate) {
        Principal subjectDN = x509Certificate.getSubjectDN();
        CertificateNode subjectNode = null;

        /** 首先需要读具有相同subjectDN的node列表 **/
        List<CertificateNode> subjectNodes = null;
        if (map.containsKey(subjectDN)) {
            subjectNodes = map.get(subjectDN);
        } else {
            subjectNodes = new LinkedList<CertificateNode>();
            map.put(subjectDN, subjectNodes);
        }

        /** 查找或新建当前证书的节点 **/
        for (CertificateNode node : subjectNodes) {
            if (x509Certificate.equals(node.getCertificate())) {
                subjectNode = node;
                break;
            } else if (node.getCertificate() == null) {
                /** 一定要保证，如果有cert为空的node，那么这个node一定要在list的最后一个**/
                subjectNode = node;
                break;
            } else {
                // do nothing
            }
        }

        /** 如果当前节点为空，则新建一个 **/
        if (subjectNode == null) {
            subjectNode = new CertificateNode();
            subjectNodes.add(subjectNode);
        }

        return subjectNode;
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public static KeyStoreCertificateChainPool createKeyStoreCertificateChainPool(KeyStore keyStore)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException,
            NoSuchProviderException, SignatureException {
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
        private X509Certificate certificate;

        private CertificateNode parentCertificateNode;

        private List<CertificateNode> childCertificateNodes = new LinkedList<CertificateNode>();

        public X509Certificate getCertificate() {
            return certificate;
        }

        public void setCertificate(X509Certificate certificate) {
            this.certificate = certificate;
        }

        public CertificateNode getParentCertificateNode() {
            return parentCertificateNode;
        }

        public void setParentCertificateNode(CertificateNode parentCertificateNode) {
            this.parentCertificateNode = parentCertificateNode;
        }

        public List<CertificateNode> getChildCertificateNodes() {
            return childCertificateNodes;
        }

        public void setChildCertificateNodes(List<CertificateNode> childCertificateNodes) {
            this.childCertificateNodes = childCertificateNodes;
        }
    }
}
