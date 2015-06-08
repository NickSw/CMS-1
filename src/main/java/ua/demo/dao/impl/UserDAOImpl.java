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
    private final String SELECT_ALL="SELECT * FROM user,role where role.id=role_id ORDER BY role_name";
    private final String SELECT_BY_ID="SELECT * FROM user,role where role.id=role_id AND user.id=";
    private final String INSERT="INSERT INTO user (first_name,last_name,login,email,password,role_id) VALUES ('${first}','${last}','${login}','${email}','${password}',${roleid})";
    private final String UPDATE_BY_ID="UPDATE user SET first_name='${first}', last_name='${last}',login='${login}',email='${email}',password='${password}',role_id=${roleid} WHERE id=${id}";
    private final String DELETE_BY_ID="DELETE FROM user WHERE id=";
    private final String SELECT_BY_LOGIN="SELECT * FROM user,role where role.id=role_id AND login=";


    private Connection con;
    public UserDAOImpl(Connection connection) {
       con=connection;
    }



    private void fillValues(User user,ResultSet rs) throws SQLException
    {
        user.setId(rs.getInt("id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setLogin(rs.getString("login"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRoleId(rs.getInt("role_id"));
        user.setRole(rs.getString("role_name"));
    }


    public User getByLogin(String login) {
        try {
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(SELECT_BY_LOGIN +"'"+ login+"'");
            User user = new User();
            while (rs.next()) {
                fillValues(user, rs);
            }
            if (user.getId()==0) return null;
            else return user;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAll() {
         try {
            Statement stat = con.createStatement();
            ResultSet rs=stat.executeQuery(SELECT_ALL);
            List<User> users=new ArrayList<User>();
            while(rs.next())
            {
                User user=new User();
                fillValues(user,rs);
                users.add(user);
            }
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public User getById(int id) {
        try {
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(SELECT_BY_ID + id);
            User user = new User();
            while (rs.next()) {
                fillValues(user, rs);
            }
            if (user.getId()==0) return null;
            else return user;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addUser(User user) {
        int res=-1;
        boolean error=false;
        if (user.getId() < 1) {
            // add new user
            try {
                Statement stat = con.createStatement();
                String query = INSERT;
                query=query.replace("${first}", user.getFirstName());
                query=query.replace("${last}", user.getLastName());
                query=query.replace("${login}", user.getLogin());
                query=query.replace("${email}", user.getEmail());
                query=query.replace("${password}", user.getPassword());
                query=query.replace("${roleid}", String.valueOf(user.getRoleId()));

                res = stat.executeUpdate(query);

            } catch (SQLException e) {
                error=true;
                e.printStackTrace();
            }
        }
        else {
            //update user
            try {
                Statement stat = con.createStatement();
                String query = UPDATE_BY_ID;
                query=query.replace("${first}", user.getFirstName());
                query=query.replace("${last}", user.getLastName());
                query=query.replace("${login}", user.getLogin());
                query=query.replace("${email}", user.getEmail());
                query=query.replace("${password}", user.getPassword());
                query=query.replace("${roleid}", String.valueOf(user.getRoleId()));
                query=query.replace("${id}", String.valueOf(user.getId()));

                res = stat.executeUpdate(query);

            } catch (SQLException e) {
                error=true;
                e.printStackTrace();
            }
        }

        if ((res>0)&&(!error)) return true;
        else return false;
    }

    public boolean deleteById(int id) {
        int res=-1;
        try {
            Statement stat=con.createStatement();
            res=stat.executeUpdate(DELETE_BY_ID+id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (res<1) return false;
        else return true;

    }


}
