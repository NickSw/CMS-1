package ua.demo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * Provides connection to MySQL DB via single channel and returns Connection object.
 *
 * Created by Sergey on 14.05.2015.
 */
public class ConnectionFactoryDriverMySQL implements ConnectionFactory{
    private final String DRIVER="com.mysql.jdbc.Driver";

    private String URL;
    private String USER;
    private String PASSWORD;


    public ConnectionFactoryDriverMySQL() {

        if ((System.getenv("OPENSHIFT_TMP_DIR"))==null){
            //local mySQL
            URL="jdbc:mysql://localhost:3306/news";
            USER="root";
            PASSWORD="password";
        } else {
            //OpenShift
            URL="jdbc:mysql://127.4.28.130:3306/news";
            USER="adminQeRj6dt";
            PASSWORD="MgDvrBAy3Urb";
        }

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
