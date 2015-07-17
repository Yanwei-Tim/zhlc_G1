package com.zhlc.g1.util;

import com.google.inject.Singleton;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

@Singleton
public class C3P0Util {
    String configFile = "c3p0";
    private ComboPooledDataSource cpds = null;

    public C3P0Util() {
        // log.debug(">>>>>>>>>>>>>>>>>>>>> <DBConnectionManager.init> Begin");
        // 建立数据库连接池
        String DRIVER_NAME = getConfigInfomation("DRIVER"); // 驱动器
        String DATABASE_URL = getConfigInfomation("URL"); // 数据库连接url
        String DATABASE_USER = getConfigInfomation("USERNAME"); // 数据库用户名
        String DATABASE_PASSWORD = getConfigInfomation("PASSWORD"); // 数据库密�码
        int Min_PoolSize = 5;
        int Max_PoolSize = 50;
        int Acquire_Increment = 5;
        int Initial_PoolSize = 10;
        int Idle_Test_Period = 3000;// 每隔3000s测试连接是否可以正常使用
        String Validate = getConfigInfomation("Validate");// 每次连接验证连接是否可用
        if (Validate.equals("")) {
            Validate = "false";
        }
        // 最小连接数
        try {
            Min_PoolSize = Integer
                    .parseInt(getConfigInfomation("Min_PoolSize"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // 增量条数
        try {
            Acquire_Increment = Integer
                    .parseInt(getConfigInfomation("Acquire_Increment"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // 最大连接数
        try {
            Max_PoolSize = Integer
                    .parseInt(getConfigInfomation("Max_PoolSize"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // 初始化连接数
        try {
            Initial_PoolSize = Integer
                    .parseInt(getConfigInfomation("Initial_PoolSize"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // 每隔3000s测试连接是否可以正常使用
        try {
            Idle_Test_Period = Integer
                    .parseInt(getConfigInfomation("Idle_Test_Period"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            cpds = new ComboPooledDataSource();
            cpds.setDriverClass(DRIVER_NAME); // 驱动器
            cpds.setJdbcUrl(DATABASE_URL); // 数据库url
            cpds.setUser(DATABASE_USER); // 用户名
            cpds.setPassword(DATABASE_PASSWORD); // 密码
            cpds.setInitialPoolSize(Initial_PoolSize); // 初始化连接池大小
            cpds.setMinPoolSize(Min_PoolSize); // 最少连接数
            cpds.setMaxPoolSize(Max_PoolSize); // 最大连接数
            cpds.setAcquireIncrement(Acquire_Increment); // 连接数的增量
            cpds.setIdleConnectionTestPeriod(Idle_Test_Period); // �测连接有效的时间间隔
            cpds.setTestConnectionOnCheckout(Boolean.getBoolean(Validate)); // 每次连接验证连接是否可用
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 返回数据库连接 对象 Connection
     *
     * @return Connection
     */
    public Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }

    /**
     * 关闭连接池
     */
    public void release() {
        try {
            if (cpds != null) {
                cpds.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getConfigInfomation(String itemIndex) {
        try {
            ResourceBundle resource = ResourceBundle.getBundle(configFile);
            return resource.getString(itemIndex);
        } catch (Exception e) {
            return "";
        }
    }
}