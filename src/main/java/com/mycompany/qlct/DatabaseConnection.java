package com.mycompany.qlct;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection getConnection() throws Exception {
        // Chuỗi kết nối chuẩn đến SQL Server của nhóm bạn
        String url = "jdbc:sqlserver://localhost:1433;"
                   + "databaseName=HTQLCHTL;"
                   + "trustServerCertificate=true;";
        
        return DriverManager.getConnection(url, "sa", "sa");
    }
}