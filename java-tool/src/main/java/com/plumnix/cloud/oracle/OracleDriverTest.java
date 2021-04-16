package com.plumnix.cloud.oracle;

import org.junit.Test;

import java.sql.*;

public class OracleDriverTest {

    @Test
    public void test_oracle_jdbc_select() throws ClassNotFoundException, SQLException {
        final String url = "jdbc:oracle:thin:@//127.0.0.1:1521/orcl";
        final String username = "system";
        final String password = "123456";
        final String sql = "select * from test";

        Class.forName("oracle.jdbc.driver.OracleDriver");
        try(Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {
            while(resultSet.next()) {
                System.out.println(resultSet.getRow());
            }
        }
    }

}
