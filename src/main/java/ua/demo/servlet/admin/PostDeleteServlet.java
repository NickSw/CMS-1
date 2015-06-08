package ua.demo.servlet.admin;

import ua.demo.dao.PostAdminDAO;
import ua.demo.dao.PostDAO;
import ua.demo.dao.UserDAO;
import ua.demo.dao.impl.PostAdminDAOImpl;
import ua.demo.dao.impl.PostDAOImpl;
import ua.demo.dao.impl.UserDAOImpl;
import ua.demo.entity.Post;
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

/**
 * Created by Sergey on 06.06.2015.
 */
public class PostDeleteServlet extends HttpServlet {
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
                PostAdminDAO postDao=new PostAdminDAOImpl(con);
                boolean ok=postDao.deleteById(id);

                //close connection
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //create message
                String[] head=new String[2];
                if (ok) {
                    head[0] = "Ok:";
                    head[1] = "post was deleted";
                } else {
                    head[0] = "Error:";
                    head[1] = "unnable to delete";
                }
                req.setAttribute("head",head);

                req.getRequestDispatcher("/view/message.jsp").forward(req, resp);

            } else {
                //wait for confirmation

                //create connection
                ConnectionFactory conf= ConnectionFactoryFactory.getConnectionFactory();
                Connection con=conf.getConnection();

                //get post with given id
                PostDAO postDAO = new PostDAOImpl(con);
                Post post=postDAO.getById(id);

                req.setAttribute("post", post);


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

                req.getRequestDispatcher("/view/post_delete.jsp").forward(req, resp);

            }

        }

    }
}

