package cn.zyf.utils.conf;

import cn.zyf.utils.conf.ConfigTree;
import cn.zyf.utils.conf.impl.PropertiesConfigUtils;
import cn.zyf.utils.conf.impl.XMLConfigUtils;

import java.io.IOException;

public abstract class ConfigUtils {

    protected ConfigTree loadConfigFile(String path) throws IOException {
        return null;
    }

    public static ConfigTree getConfig(String path) throws IOException {

        if (path == null) {
            throw new IOException("config path is null");
        }
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
