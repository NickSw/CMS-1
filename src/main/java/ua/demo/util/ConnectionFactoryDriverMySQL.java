package ua.demo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactoryDriverMySQL implements ConnectionFactory{
    private final String DRIVER="com.mysql.jdbc.Driver";
    private final String URL="jdbc:mysql://localhost:3306/news";
    private final String USER="root";
    private final String PASSWORD="password";

    public ConnectionFactoryDriverMySQL() {
    }

    public Connection getConnection(){

        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close(){
        //nothing to close
    }
}
