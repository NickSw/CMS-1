package ua.demo.servlet.admin;

import ua.demo.dao.PostAdminDAO;
import ua.demo.dao.TagDAO;
import ua.demo.dao.impl.PostAdminDAOImpl;
import ua.demo.dao.impl.TagDAOImpl;
import ua.demo.entity.Post;
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
 * servlet creates form for post, if "id" is defined in query string,  form will be filled with post information,
 * otherwise form will be empty
 *
 * Created by Sergey on 06.06.2015.
 */
public class PostFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get current user from session
        User curUser=(User)req.getSession(false).getAttribute("curuser");
        req.setAttribute("curuser",curUser);

        //check request string parameters
        //id=-1 add new user
        //id>0 update user with id
        String param = req.getParameter("id");
        int id = -1;
        try {
            id = Integer.parseInt(param);
        } catch (Exception ex) {id=-1;}

        //creates connection
        ConnectionFactory conf = ConnectionFactoryFactory.getConnectionFactory();
        Connection con = conf.getConnection();

        //creates list of tags
        TagDAO tagDAO=new TagDAOImpl(con);
        List<Tag> tags=tagDAO.getAll();
        req.setAttribute("tags",tags);

        String[] head=new String[2];

        if (id>0) {
            //update existing post

            //get post with given id
            PostAdminDAO postDao = new PostAdminDAOImpl(con);
            Post post = postDao.getById(id);

            //check if author are allowed to edit this post
            if (curUser.getRole().equals("author")) {
                if (post.getUserId()!=curUser.getId()) {
                    //access denied
                    MessageSender.sendMessage("Access denied: "+curUser.getLogin(), " post has different author", req, resp);
                    return;
                }
            }

            req.setAttribute("post", post);

            //creates message
            head[0]="Update:";
            head[1]="";


        } else {
            //create new post
            //corrector are not allowed to create new post
            if (curUser.getRole().equals("corrector")) {
                //access denied
                MessageSender.sendMessage("Access denied: " + curUser.getLogin(), curUser.getRole()+" are not allowed to create new post.", req, resp);
                return;
            }

            head[0]="New:";
            head[1]="";
        }
        //close connection
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        req.setAttribute("head",head);
        req.getRequestDispatcher("/view/post_update.jsp").forward(req, resp);
    }
}
