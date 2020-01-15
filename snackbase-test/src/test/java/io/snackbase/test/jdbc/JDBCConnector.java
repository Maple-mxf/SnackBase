package io.snackbase.test.jdbc;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author maxuefeng
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
        Statement sta = conn.createStatement();

        Thread.sleep(10000000);
    }
}
