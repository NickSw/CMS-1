package ua.demo.servlet.admin;

import ua.demo.dao.RoleDAO;
import ua.demo.dao.TagDAO;
import ua.demo.dao.UserDAO;
import ua.demo.dao.impl.RoleDAOImpl;
import ua.demo.dao.impl.TagDAOImpl;
import ua.demo.dao.impl.UserDAOImpl;
import ua.demo.entity.Role;
import ua.demo.entity.Tag;
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
 * Created by Sergey on 08.06.2015.
 */
public class TagServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User curUser=(User)req.getSession(false).getAttribute("curuser");
        req.setAttribute("curuser",curUser);

        //check request string parameters
        //id=-1 add new tag
        //id>0 update tag with id
        String param = req.getParameter("id");
        int id = -1;
        try {
            id = Integer.parseInt(param);
        } catch (Exception ex) {id=-1;}


        //create connection
        ConnectionFactory conf= ConnectionFactoryFactory.getConnectionFactory();
        Connection con=conf.getConnection();

        if (id>0)
        {
            //get tag with given id
            TagDAO tagDao=new TagDAOImpl(con);
            Tag tag=tagDao.getById(id);
            req.setAttribute("tag", tag);

            String[] head=new String[2];
            head[0]="Update:";
            head[1]="";
            req.setAttribute("head",head);
        } else {
            String[] head=new String[2];
            head[0]="New:";
            head[1]="";
            req.setAttribute("head",head);
        }


        //close connection
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("/view/tag_update.jsp").forward(req, resp);


    }

}
