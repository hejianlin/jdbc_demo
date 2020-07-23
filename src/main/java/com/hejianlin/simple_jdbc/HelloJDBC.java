package com.hejianlin.simple_jdbc;

import java.sql.*;

/**
 * 简单JDBC案例
 */
public class HelloJDBC {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //useCursorFetch=true表示使用游标
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloud_study?useCursorFetch=true";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static void databaseOperation() {

        Connection connection = null;
        //Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 1. 装载驱动程序
            Class.forName(JDBC_DRIVER);

            // 2. 建立数据库连接
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            // 3. 执行sql语句
            //statement = connection.createStatement();
            //resultSet = statement.executeQuery("select name from user");
            preparedStatement = connection.prepareStatement("select name from user");
            preparedStatement.setFetchSize(1);
            resultSet = preparedStatement.executeQuery();

            // 4. 获取执行结果
            while (resultSet.next()) {
                System.out.println("Hello " + resultSet.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5. 清理环境
            StreamJDBC.closeResource(connection, preparedStatement, resultSet,null,null);
        }

    }

    public static void main(String[] args) {
        databaseOperation();
    }

}
