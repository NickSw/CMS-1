package ua.demo.util;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactory {
    public Connection getConnection();
    public void close();
}
