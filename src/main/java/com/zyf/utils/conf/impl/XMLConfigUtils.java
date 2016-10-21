package com.zyf.utils.conf.impl;

import com.zyf.utils.conf.ConfigTree;
import com.zyf.utils.conf.ConfigTreeNode;
import com.zyf.utils.conf.ConfigUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;
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

            tree.getRoot().setSubConfigTree(parseNodeList(document.getDocumentElement().getChildNodes()));
            
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException(e.getMessage());
        }

        System.out.println(tree.toString());
        return tree;
    }

    private Map<String, ConfigTreeNode> parseNodeList(NodeList nodeList) {
        Map<String, ConfigTreeNode> ret = new ConcurrentHashMap<>();

        for (int i=0; i<nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                ConfigTreeNode configTreeNode = (node.getChildNodes().getLength() == 1) ?
                        new ConfigTreeNode(node.getTextContent(), null)
                        :
                        new ConfigTreeNode("", parseNodeList(node.getChildNodes()));

                ret.put(node.getNodeName(), configTreeNode);
            }
        }

        return ret;
    }
}