package io.snackbase.test.jdbc;

import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.function.Function;

/**
 * @author maxuefeng
 * @see AbstractQueuedSynchronizer
 * @see java.util.concurrent.ConcurrentHashMap
 * @see java.util.LinkedHashMap
 * @see Thread#sleep(long)
 * @since 2020/1/1
 */
public class JDBCConnector {

    String url = "jdbc:mysql://localhost:3451/java0603?useUnicode=true&characterEncoding=UTF-8";

    @Before
    public void before() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
    }

    @Test
    public void testPingSnackServer() throws SQLException, InterruptedException {
        String user = "root";
        String password = "123456";
        Connection conn = DriverManager.getConnection(url, user, password);
        System.err.println(conn);

        PreparedStatement ps = conn.prepareStatement("select * from users");
        ResultSet resultSet = ps.executeQuery();
        System.err.println(resultSet);

        Thread.sleep(10000000);
    }

    // 接口可以多继承
    interface IA extends java.io.Serializable, Function<String, String> {
    }

    // 类不可以多继承
    // class CA extends RuntimeException,Number{}
}
