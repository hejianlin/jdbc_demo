package com.hejianlin.simple_jdbc;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * JDBC批处理案例
 */
public class BatchJDBC {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloud_study?characterEncoding=utf8";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static void insertUser(List<String> userNames) {

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // 1. 装载驱动程序
            Class.forName(JDBC_DRIVER);

            // 2. 建立数据库连接
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            // 3. 执行sql语句
            statement = connection.createStatement();
            for (String userName : userNames) {
                statement.addBatch("insert into user(name) values('"+userName+"')");
            }
            statement.executeBatch();
            statement.clearBatch();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5. 清理环境
            StreamJDBC.closeResource(connection, statement, resultSet, null, null);
        }
    }



    public static void main(String[] args) {
        insertUser(Arrays.asList("xiaobai","xiaohei","xiaohuang"));
    }

}
