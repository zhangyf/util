package com.zyf.utils.conf.impl;

import com.zyf.utils.conf.ConfigTree;
import com.zyf.utils.conf.ConfigTreeNode;
import com.zyf.utils.conf.ConfigUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangyufeng on 2016/10/21.
 */
public class XMLConfigUtils extends ConfigUtils {

    @Override
    protected ConfigTree loadConfigFile(String path) throws IOException {
        ConfigTree tree = new ConfigTree();
        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(path);

            tree.getRoot().setName(document.getDocumentElement().getNodeName());
            tree.getRoot().addValue(parseNodeList(document.getDocumentElement().getChildNodes()));

        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException(e.getMessage());
        }

        return tree;
    }

    private Set<ConfigTreeNode> parseNodeList(NodeList nodeList) {
        Set<ConfigTreeNode> ret = new HashSet<>();

        for (int i=0; i<nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                ConfigTreeNode subNode = new ConfigTreeNode();
                subNode.setName(node.getNodeName());
                if (node.getChildNodes().getLength() == 1) {
                    subNode.addValue(node.getTextContent());
                    if (node.hasAttributes()) {
                        NamedNodeMap attributes = node.getAttributes();
                        for (int j=0; j<attributes.getLength(); j++) {
                            subNode.addAttribute(attributes.item(j).getNodeName(), attributes.item(j).getNodeValue());
                        }
                    }
                } else {
                    for (ConfigTreeNode configTreeNode : parseNodeList(node.getChildNodes())) {
                        subNode.addValue(configTreeNode);
                        if (node.hasAttributes()) {
                            NamedNodeMap attributes = node.getAttributes();
                            for (int j=0; j<attributes.getLength(); j++) {
                                subNode.addAttribute(attributes.item(j).getNodeName(), attributes.item(j).getNodeValue());
                            }
                        }
                    }
                }
                ret.add(subNode);
            }
        }

        return ret;
    }
}
