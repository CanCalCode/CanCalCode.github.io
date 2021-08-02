package com.lyhstu.mhl.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class JDBCUtilsByDruid {
    private static DataSource ds;

    //静态初始化
    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src\\druid.properties"));
            ds = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //返回一个Connection对象
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();

    }

    //关闭各种资源连接
    public static void close(ResultSet set, Statement statement,Connection connection) {
        try {
            if (set != null) {
                set.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.toString();
    }
}
