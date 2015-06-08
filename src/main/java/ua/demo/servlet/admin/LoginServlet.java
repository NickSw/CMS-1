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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Sergey on 07.06.2015.
 */
public class LoginServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("/view/login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get and check parameters
        String login=req.getParameter("login");
        String password=req.getParameter("password");

        if ((login==null)||(login.isEmpty())||(password==null)||(password.isEmpty()))
        {
            String errorMsg="Enter login and password";
            req.setAttribute("error",errorMsg);
            req.getRequestDispatcher("/view/login.jsp").forward(req,resp);
        } else {

            //creates connection
            ConnectionFactory conf= ConnectionFactoryFactory.getConnectionFactory();
            Connection con=conf.getConnection();

            //get user with given login
            //deffence of sql injection
            login=login.replace("'"," ");

            UserDAO userDao=new UserDAOImpl(con);
            User user=userDao.getByLogin(login);

            //close connection
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            boolean error=false;
            String errorMsg="";
            if (user==null) {
                error=true;
                errorMsg="User with this login does not exist.";
            } else {
                if (!password.equals(user.getPassword())) {
                    error=true;
                    errorMsg="Incorrect password";
                } else {
                    //user`s login and password correct
                    HttpSession session=req.getSession(true);
                    session.setAttribute("curuser", user);

                    resp.sendRedirect("/admin");
                    return;
                }
            }

           System.out.println(errorMsg);
            //there was an error
            req.setAttribute("error",errorMsg);

            req.getRequestDispatcher("/view/login.jsp").forward(req,resp);

        }



    }
}
