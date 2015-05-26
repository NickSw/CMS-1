package ua.demo.servlet;


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

public class userServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //create connection
        ConnectionFactory conf= ConnectionFactoryFactory.getConnectionFactory();
        Connection con=conf.getConnection();

        //creates list of users
        UserDAO userDao=new UserDAOImpl(con);
        List<User> users=userDao.getAll();

       //close connection
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        req.setAttribute("users", users);
        req.getRequestDispatcher("/view/user.jsp").forward(req,resp);
    }
}
