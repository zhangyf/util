package cn.zyf.utils.db;

/**
 * Created by zhangyufeng on 2016/9/29.
 *
 */

public class DBConfig {
    final static String defaultDBDriver = "com.mysql.cj.jdbc.Driver";

    private String username = "";
    private String password = "";
    private String type = "";
    private String host = "";
    private String dbName = "";
    private String autoTestTable = "";
    private String driver = "";
    private int port = 3306;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getAutoTestTable() {
        return autoTestTable;
    }

    public void setAutoTestTable(String autoTestTable) {
        this.autoTestTable = autoTestTable;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }


}
