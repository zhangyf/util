package com.zyf.utils.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.List;

/**
 * Created by zhangyufeng on 2016/9/29.
 *
 */

public class DBUtils {
    private static DBUtils instance = null;
    private ComboPooledDataSource cpds = null;

    public static DBUtils getInstance() {
        return instance;
    }

    public static void init(DBConfig config) throws PropertyVetoException {
        if (instance == null) {
            synchronized (DBUtils.class) {
                if (instance == null) {
                    instance = new DBUtils(config);
                }
            }
        }
    }

    public ResultSet query(String sql) throws SQLException {
        ResultSet ret;
        Statement statement = getConnection().createStatement();
        ret = statement.executeQuery(sql);

        return ret;
    }

    public ResultSet query(String sql, List<Object> params) throws SQLException {
        ResultSet ret;
        if (params != null && !params.isEmpty()) {
            PreparedStatement pst = getConnection().prepareStatement(sql);
            int idx = 1;
            for (Object o : params) {
                pst.setObject(idx++, o);
            }
            ret = pst.executeQuery();
        } else {
            ret = query(sql);
        }

        return ret;
    }

    private DBUtils(DBConfig config) throws PropertyVetoException {
        String url = "jdbc:" + config.getType()
                + "://" + config.getHost()
                + ":" + config.getPort()
                + "/" + config.getDbName()
                + "?autoReconnect=true&&autoReconnectForPools=true";

        String DRIVER = !config.getDriver().equals("") ? config.getDriver() : DBConfig.defaultDBDriver;

        cpds = new ComboPooledDataSource();
        cpds.setDriverClass(DRIVER);
        cpds.setJdbcUrl(url);
        cpds.setUser(config.getUsername());
        cpds.setPassword(config.getPassword());
        cpds.setAutomaticTestTable(config.getAutoTestTable());
        cpds.setPreferredTestQuery("SELECT 1 FROM `" + config.getAutoTestTable() + "`");
        cpds.setTestConnectionOnCheckout(true);
        cpds.setAutoCommitOnClose(false);
    }

    private synchronized Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }
}
