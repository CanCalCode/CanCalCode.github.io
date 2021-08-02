package com.lyhstu.mhl.utils;

import java.sql.SQLException;

public class Test {
    public static void main(String[] args) {
        //测试Utility工具类
        int i = Utility.readInt();
        System.out.println(i);

        //测试JDBCUtilsByDruid工具类
        try {
            System.out.println(JDBCUtilsByDruid.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
