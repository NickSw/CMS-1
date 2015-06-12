package ua.demo.servlet.admin;

import ua.demo.dao.TagDAO;
import ua.demo.dao.impl.TagDAOImpl;
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

/**
 * servlet provides two step tag deletion.
 * receives parameters from query srtring: id - is tag id to delete, del - true of false,
 * if del==empty (i.e. false) servlet generates page with tag to delete and asks for delete confirmation,
 * after user pressed "delete button", page sends to this servlet del=true, and tag will delete
 *
 * Created by Sergey on 08.06.2015.
 */
public class TagDeleteServlet extends HttpServlet{
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

            //create message
            MessageSender.sendMessage("ERROR:", "nothing to delete", req, resp);
            return;

        } else {
            if ((del!=null)&&(!del.isEmpty())){
                //do delete

                //create connection
                ConnectionFactory conf= ConnectionFactoryFactory.getConnectionFactory();
                Connection con=conf.getConnection();

                //delete tag with given id
                TagDAO tagDao=new TagDAOImpl(con);
                boolean wasDeleted=tagDao.deleteById(id);

                //close connection
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                String[] head=new String[2];
                if (wasDeleted) {
                    MessageSender.sendMessage("Ok", "tag was deleted", req, resp);
                    return;
                } else {
                   MessageSender.sendMessage("Error", "unnable to delete tag", req, resp);
                    return;
                }

            } else {
                //wait for confirmation

                //create connection
                ConnectionFactory conf= ConnectionFactoryFactory.getConnectionFactory();
                Connection con=conf.getConnection();

                //get tag with given id
                TagDAO tagDao=new TagDAOImpl(con);
                Tag tag=tagDao.getById(id);
                req.setAttribute("tag", tag);

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

                req.getRequestDispatcher("/view/tag_delete.jsp").forward(req, resp);

            }

        }

    }
}
