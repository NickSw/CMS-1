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

public class MainServlet extends HttpServlet{
   @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       //creates connection
       ConnectionFactory conf= ConnectionFactoryFactory.getConnectionFactory();
       Connection con=conf.getConnection();

        //creates list of tags
       TagDAO tagDAO=new TagDAOImpl(con);
       List<Tag> tags=tagDAO.getAll();
       req.setAttribute("tags",tags);

       //creates list of recent posts
       PostDAO postDAO=new PostDAOImpl(con);
       List<Post> recPosts=postDAO.getRecent();
       req.setAttribute("recent_posts",recPosts);

       //creates archive dates
       List<Date> dates=postDAO.getArchiveDates();
       req.setAttribute("archive_dates",dates);

       //creates list of main posts
       List<Post> mainPosts=postDAO.getMain();
       req.setAttribute("main_posts",mainPosts);

       //close connection
       try {
           con.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }

       req.getRequestDispatcher("/view/main.jsp").forward(req,resp);

    }
}
