package ua.demo.dao;

import ua.demo.entity.Role;
import java.util.List;

/**
 * Created by Sergey on 02.06.2015.
 */
public interface RoleDAO {
    public RoleDAO getById(int id);
    public List<Role> getAll();
}
