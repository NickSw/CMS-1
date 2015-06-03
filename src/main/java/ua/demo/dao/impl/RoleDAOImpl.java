package ua.demo.dao.impl;

import ua.demo.dao.RoleDAO;
import ua.demo.entity.Role;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 02.06.2015.
 */
public class RoleDAOImpl implements RoleDAO {
    private final String SELECT_ALL="SELECT * FROM role";
    private Connection con;

    public RoleDAOImpl(Connection con) {
        this.con=con;
    }

    public RoleDAO getById(int id) {
        return null;
    }

    public List<Role> getAll() {
        try {
            Statement stat=con.createStatement();
            ResultSet rs=stat.executeQuery(SELECT_ALL);
            List<Role> roles=new ArrayList<Role>();
            while(rs.next())
            {
                Role role=new Role();
                role.setId(rs.getInt("id"));
                role.setRoleName(rs.getString("role_name"));

                roles.add(role);
            }
            return roles;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
