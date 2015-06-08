package ua.demo.servlet.admin;

import ua.demo.dao.RoleDAO;
import ua.demo.dao.UserDAO;
import ua.demo.dao.impl.RoleDAOImpl;
import ua.demo.dao.impl.UserDAOImpl;
import ua.demo.entity.Role;
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

/**
 * Created by Sergey on 03.06.2015.
 */
public class UserDeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User curUser=(User)req.getSession(false).getAttribute("curuser");
        req.setAttribute("curuser",curUser);

        //get query string parameters
        String idStr=req.getParameter("id");
        String del=req.getParameter("del");

        int id=-1;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception ex) {}

        if (id<1) {
            //nothing to delete

            //create message
            String[] head=new String[2];
            head[0]="ERROR:";
            head[1]="nothing to delete";
            req.setAttribute("head",head);

            req.getRequestDispatcher("/view/message.jsp").forward(req, resp);
        } else {
            if ((del!=null)&&(!del.isEmpty())){
                //do delete

                //create connection
                ConnectionFactory conf= ConnectionFactoryFactory.getConnectionFactory();
                Connection con=conf.getConnection();

                //delete user with given id
                UserDAO userDao=new UserDAOImpl(con);
                userDao.deleteById(id);

                //close connection
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                resp.sendRedirect("/admin/users");
                return;

            } else {
                //wait for confirmation

                //create connection
                ConnectionFactory conf= ConnectionFactoryFactory.getConnectionFactory();
                Connection con=conf.getConnection();

                //get user with given id
                UserDAO userDao=new UserDAOImpl(con);
                User user=userDao.getById(id);
                req.setAttribute("user", user);

                //close connection
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                //create message
                String[] head=new String[2];
                head[0]="Deletion";
                head[1]="";
                req.setAttribute("head",head);

                req.getRequestDispatcher("/view/user_delete.jsp").forward(req, resp);

            }

        }

    }
}
