package com.hejianlin.simple_jdbc;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sound.midi.Soundbank;
import java.sql.*;

/**
 * JDBC使用DBCP连接池案例
 */
public class ThreadJDBC {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloud_study";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static BasicDataSource dbPool = null;

    public static void dbPoolInit() {
        // 1.初始化Dbcp连接池
        dbPool = new BasicDataSource();
        dbPool.setUrl(DB_URL);
        dbPool.setDriverClassName(JDBC_DRIVER);
        dbPool.setUsername(USER);
        dbPool.setPassword(PASSWORD);

        //设置最大连接数为10
        dbPool.setMaxTotal(2);

    }

    /**
     * 纯粹JDBC查询
     */
    public static void jdbcTest() {
        //在10秒内不断地进行查询
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 10000){
            try {
                 Class.forName(JDBC_DRIVER);
                databaseOperation(DriverManager.getConnection(DB_URL, USER, PASSWORD));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * dbcp查询
     */
    public static void dbcpTest() {
        //在10秒内不断地进行查询
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 10000){
            try {
                databaseOperation(dbPool.getConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void databaseOperation(Connection connection) {

        Statement statement = null;
        ResultSet resultSet = null;

        try {

            // 3. 执行sql语句
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select name from user");

            // 4. 获取执行结果
            while (resultSet.next()) {
                System.out.println("Hello " + resultSet.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5. 清理环境
            StreamJDBC.closeResource(connection, statement, resultSet, null, null);
        }

    }

    public static void main(String[] args) {
        dbPoolInit();
        for(int i=0;i<10;i++){
            Thread thread = new Thread(() -> {
                //jdbcTest();
                dbcpTest();
            });
            thread.start();
        }
    }

}
