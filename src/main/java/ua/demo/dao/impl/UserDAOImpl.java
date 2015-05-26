package ua.demo.dao.impl;

import ua.demo.dao.UserDAO;
import ua.demo.entity.User;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private final String SELECT_ALL="SELECT * FROM user,role where role.id=role_id";
    private Connection con;
    public UserDAOImpl(Connection connection) {
       con=connection;
    }

    public List<User> getAll() {
         try {
            Statement stat = con.createStatement();
            ResultSet rs=stat.executeQuery(SELECT_ALL);
            List<User> users=new ArrayList<User>();
            while(rs.next())
            {
                User user=new User();
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setLogin(rs.getString("login"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role_name"));

                users.add(user);
            }
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
}
