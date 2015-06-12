package ua.demo.servlet;

import ua.demo.dao.PostDAO;
import ua.demo.dao.impl.PostDAOImpl;
import ua.demo.dao.TagDAO;
import ua.demo.dao.impl.TagDAOImpl;
import ua.demo.entity.Post;
import ua.demo.entity.Tag;
import ua.demo.util.ConnectionFactory;
import ua.demo.util.ConnectionFactoryFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;


/*
* servlet show full post
* post_id is received via query string parameter `id`
*
* Created by Sergey on 13.05.2015.
*/
public class PostServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //check request string parameters
        String param=req.getParameter("id");
        int id;
        if ((param==null)||(param.isEmpty())) {
            resp.sendRedirect("/");
        }
        else {
            try {
                id = Integer.parseInt(param);
            } catch (Exception ex){resp.sendRedirect("/");return;}

            //creates connection
            ConnectionFactory conf = ConnectionFactoryFactory.getConnectionFactory();
            Connection con = conf.getConnection();

            //creates list of tags
            TagDAO tagDAO = new TagDAOImpl(con);
            List<Tag> tags = tagDAO.getAll();
            req.setAttribute("tags", tags);

            //creates list of recent posts
            PostDAO postDAO = new PostDAOImpl(con);
            List<Post> recPosts = postDAO.getRecent();
            req.setAttribute("recent_posts", recPosts);

            //creates archive dates
            List<Date> dates=postDAO.getArchiveDates();
            req.setAttribute("archive_dates",dates);

            //creates full post
            Post fullPost = postDAO.getById(id);
            //check if post with given id exists
            if (fullPost==null) {resp.sendRedirect("/");return;}
            else req.setAttribute("post", fullPost);

            //creates related tags
            List<Tag> relatedTags = tagDAO.getByPost(id);
            req.setAttribute("related_tags", relatedTags);

            //close connection
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            req.getRequestDispatcher("/view/post.jsp").forward(req, resp);
        }
    }
}
