package ua.demo.util;

/**
 *
 * ConnectionFactoryFactory that produces some kind of ConnectionFactory object contained in `enum`.
 *
 * Created by Sergey on 14.05.2015.
 */

public class ConnectionFactoryFactory {
    enum Type {driverMySQL,poolMySQL}
    private static Type defaultType=Type.driverMySQL;

    public static ConnectionFactory getConnectionFactory(Type type)
    {
       switch (type)
       {
           case driverMySQL: return new ConnectionFactoryDriverMySQL();
           case poolMySQL: return new ConnectionFactoryPoolMySQL();

       }
        //never here
        return null;
    }

    public static ConnectionFactory getConnectionFactory()
    {
        switch (defaultType)
        {
            case driverMySQL: return new ConnectionFactoryDriverMySQL();
            case poolMySQL: return new ConnectionFactoryPoolMySQL();

        }
        //never here
        return null;
    }
}
