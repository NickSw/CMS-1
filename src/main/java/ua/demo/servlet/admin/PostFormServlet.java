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
 * Created by Sergey on 06.06.2015.
 */
public class PostFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

            //check if author edit his post
            if (curUser.getRole().equals("author")) {
                if (post.getUserId()!=curUser.getId()) {
                    //access denied
                    head=new String[2];
                    head[0]="Access denied: "+curUser.getLogin();
                    head[1]= " post has diffrent author";
                    req.setAttribute("head",head);

                    req.getRequestDispatcher("/view/message.jsp").forward(req,resp);
                }
            }








            req.setAttribute("post", post);

            //creates message
            head[0]="Update:";
            head[1]="";


        } else {
            //create new post
            //corrector are not allowed
            if (curUser.getRole().equals("corrector")) {
                //access denied
                head=new String[2];
                head[0]="Access denied: "+curUser.getLogin();
                head[1]= curUser.getRole()+" are not allowed to create new post.";
                req.setAttribute("head",head);

                req.getRequestDispatcher("/view/message.jsp").forward(req,resp);

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
