package com.hejianlin.simple_jdbc;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.concurrent.TransferQueue;

/**
 * JDBC使用DBCP连接池案例
 */
public class DbcpJDBC {

    private static BasicDataSource dbPool = null;
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloud_study";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";


    public static void dbPoolInit(){
        // 1.初始化Dbcp连接池
        dbPool = new BasicDataSource();
        dbPool.setUrl(DB_URL);
        dbPool.setDriverClassName(JDBC_DRIVER);
        dbPool.setUsername(USER);
        dbPool.setPassword(PASSWORD);

        //高级设置
        //设置初始连接数为1
        dbPool.setInitialSize(1);
        //设置最大连接数为10
        dbPool.setMaxTotal(10);
        //设置队列的最大等待时间为10秒
        dbPool.setMaxWaitMillis(10000);
        //设置最大空闲连接数为1，超过部分的连接，则自动被回收
        dbPool.setMaxIdle(1);
        //设置最小空闲连接数为1，小于它，则自动创建连接，一般来说，为了避免频繁地创建或者释放连接，建议将maxIdle和minIdle都设置为1
        dbPool.setMinIdle(1);

        //定期检查
        //如果没有设置，mysql服务器会自动关闭空闲时间超过8小时的连接，这时候客户端却并不清楚连接已经被服务端关闭了
        //当应用程序向连接池租借连接时，连接池可能会将失效的连接直接租借给客户端，客户端使用这个连接操作时，就会报相应的sql异常
        //通过定期对连接池的空闲连接进行检查,在服务端关闭连接之前，我们保证将这些连接销毁掉，重新补充新的连接
        //当连接空闲时，进行检查
        dbPool.setTestWhileIdle(true);
        //最小的空闲时间，当空闲时间超过该值时，自动销毁连接
        dbPool.setMinEvictableIdleTimeMillis(10000);
        //检查空闲时间的时间间隔，建议设置为小于服务器自动关闭连接的阈值时间，也就是说，如果没有设置空闲时间，mysql的默认空闲时间是8个小时，所以设置需要小于8个小时
        dbPool.setTimeBetweenEvictionRunsMillis(1000);

    }

    public static void databaseOperation() {

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // 2. 建立数据库连接
            connection = dbPool.getConnection();

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
            StreamJDBC.closeResource(connection, statement, resultSet,null,null);
        }

    }

    public static void main(String[] args)
    {
        dbPoolInit();
        databaseOperation();
    }

}
