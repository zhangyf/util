package com.zyf.utils.conf;

import com.zyf.utils.conf.impl.PropertiesConfigUtils;
import com.zyf.utils.conf.impl.XMLConfigUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public abstract class ConfigUtils {

    protected ConfigTree loadConfigFile(String path) throws IOException {
        return null;
    }

    public static ConfigTree getConfig(String path) throws IOException {

        String[] nameEntries = path.split("\\.");
        String configFileType = nameEntries[nameEntries.length - 1];

        ConfigUtils configInstance = null;

        switch (configFileType.toLowerCase()) {
            case "xml":
                configInstance = new XMLConfigUtils();
                break;
            case "properties":
                configInstance = new PropertiesConfigUtils();
                break;
            default:
                throw new IOException("unknown config file type");
        }

        return configInstance.loadConfigFile(path);
    }

}
