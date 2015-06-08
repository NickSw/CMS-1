package ua.demo.servlet.admin;


import ua.demo.dao.UserDAO;
import ua.demo.dao.impl.UserDAOImpl;
import ua.demo.entity.User;
import ua.demo.util.ConnectionFactory;
import ua.demo.util.ConnectionFactoryFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User curUser=(User)req.getSession(false).getAttribute("curuser");
        req.setAttribute("curuser",curUser);

        //create connection
        ConnectionFactory conf= ConnectionFactoryFactory.getConnectionFactory();
        Connection con=conf.getConnection();

        //creates list of users
        UserDAO userDao=new UserDAOImpl(con);
        List<User> users=userDao.getAll();
        req.setAttribute("users", users);

       //close connection
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        req.getRequestDispatcher("/view/users.jsp").forward(req,resp);
    }
}
