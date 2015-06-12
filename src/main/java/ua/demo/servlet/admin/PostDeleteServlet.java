package ua.demo.servlet.admin;

import ua.demo.dao.PostAdminDAO;
import ua.demo.dao.PostDAO;
import ua.demo.dao.impl.PostAdminDAOImpl;
import ua.demo.dao.impl.PostDAOImpl;
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
 * servlet provides two step post deletion.
 * receives parameters from query srtring: id - is post id to delete, del - true of false,
 * if del==empty (i.e. false) servlet generates page with post to delete and asks for delete confirmation,
 * after user pressed "delete button", page sends to this servlet del=true, and post will delete
 *
 * Created by Sergey on 06.06.2015.
 */
public class PostDeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get current user from session
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
            MessageSender.sendMessage("ERROR:", "nothing to delete", req, resp);
            return;
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
                    MessageSender.sendMessage("Ok:", "post was deleted", req, resp);
                    return;
                } else {
                    MessageSender.sendMessage("Error:",  "unnable to delete", req, resp);
                    return;
                }

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

