package com.plumnix.project.postgres;

import org.junit.Test;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.Properties;
import java.util.stream.IntStream;

public class CopyManagerTest {

    @Test
    public void test_csv_input() throws Exception {
        this.copyFromFile("C:\\data\\file_bulk.csv", "test");
    }

    @Test
    public void test_csv_output() throws Exception {
        this.copyToFile("C:\\data\\file.csv", "test");
    }

    @Test
    public void test_csv_create() throws IOException {
        StringBuilder sbf = new StringBuilder();
        IntStream.range(0, 1_000_000).forEach(no -> {
            sbf.append(no + ",\"a\",\"b\",\"v\",\"d\",\"e\",\"f\",\"g\",\"g\",\"h\",\"j\",\"j\",32,2020-04-07\r\n");
        });
        Files.write(Paths.get("C:\\data\\file_bulk.csv"), sbf.toString().getBytes());
    }

    /**
     * 导入文件到数据库
     *
     * @param filePath
     * @param tableName
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public long copyFromFile(String filePath, String tableName)
            throws Exception {
        LocalTime before = null;
        try (Connection connection = this.getConnection();
             FileInputStream fileInputStream = new FileInputStream(filePath)) {
            before = LocalTime.now();
            CopyManager copyManager = new CopyManager((BaseConnection) connection);
            return copyManager.copyIn("COPY " + tableName + " FROM STDIN DELIMITER AS ','", fileInputStream);
        } finally {
            LocalTime current = LocalTime.now();
            Duration p = Duration.between(before, current);
            System.out.print(p.getSeconds());
        }
    }

    /**
     * 导出到txt
     *
     * @param filePath
     * @param tableOrQuery 可以为tablename ,also (sql语句)
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public long copyToFile(String filePath, String tableOrQuery)
            throws Exception {
        try (Connection connection = this.getConnection();
             FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            CopyManager copyManager = new CopyManager((BaseConnection) connection);
            return copyManager.copyOut("COPY " + tableOrQuery + " TO STDOUT  WITH DELIMITER ',' quote '\"' csv header", fileOutputStream);
        }
    }

    public Connection getConnection() throws Exception {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://152.136.46.93:5432/postgres";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "Ab27911564");
        return DriverManager.getConnection(url, props);
    }

}
