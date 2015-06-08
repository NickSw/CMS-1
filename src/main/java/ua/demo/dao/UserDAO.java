package ua.demo.dao;

import ua.demo.entity.User;

import java.util.List;

public interface UserDAO {
    public List<User> getAll();
    public User getById(int id);
    public boolean addUser(User user);
    public void deleteById(int id);
    public User getByLogin(String login);
}
