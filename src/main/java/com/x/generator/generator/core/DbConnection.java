package com.x.generator.generator.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author lsx
 * @date 2024-07-13
 */
public class DbConnection {


    public static Connection getConnection(String url, String user, String password) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/xindada?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "root";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public static void close(Connection conn) {
       if (conn != null) {
           try {
               conn.close();
           } catch (SQLException e) {
               throw new RuntimeException(e);
           }
       }
    }
}
