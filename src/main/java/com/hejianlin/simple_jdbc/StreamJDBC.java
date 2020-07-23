package com.hejianlin.simple_jdbc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;

/**
 * JDBC流方式读取数据案例
 */
public class StreamJDBC {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloud_study";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final String FILE_PREFIX = "D:\\info";
    private static final String FILE_SUFFIX = ".txt";

    public static void databaseOperation() {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        InputStream in = null;
        OutputStream out = null;

        try {
            // 1. 装载驱动程序
            Class.forName(JDBC_DRIVER);

            // 2. 建立数据库连接
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            // 3. 执行sql语句
            preparedStatement = connection.prepareStatement("select info from info_message");
            resultSet = preparedStatement.executeQuery();

            int i = 1;
            // 4. 获取执行结果
            while (resultSet.next()) {

                // 5.获取流对象
                in = resultSet.getBinaryStream("info");

                // 6.将对象流写入文件
                File f = new File(FILE_PREFIX + i + FILE_SUFFIX);
                out = new FileOutputStream(f);
                int temp = 0;

                //边读边写
                while ((temp = in.read()) != -1) {
                    out.write(temp);
                }
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 6. 清理环境
            closeResource(connection, preparedStatement, resultSet,in,out);
        }

    }

    public static void closeResource(Connection connection, Statement statement, ResultSet resultSet,
                                     InputStream in, OutputStream out) {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        databaseOperation();
    }

}
