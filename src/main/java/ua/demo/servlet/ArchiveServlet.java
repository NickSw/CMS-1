package ua.demo.servlet;

import ua.demo.dao.PostDAO;
import ua.demo.dao.impl.PostDAOImpl;
import ua.demo.dao.TagDAO;
import ua.demo.dao.impl.TagDAOImpl;
import ua.demo.entity.Post;
import ua.demo.entity.Tag;
import ua.demo.util.ConnectionFactory;
import ua.demo.util.ConnectionFactoryFactory;
import ua.demo.util.PagingModel;

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
* servlet show all posts
*
* Created by Sergey on 13.05.2015.
*/
public class ArchiveServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //check parameter value
        String param=req.getParameter("page");
        int page;
        try {
            page = Integer.parseInt(param);
        }catch (Exception ex) {page=1;}
        if (page<1) page=1;

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

        //creates head
        String[] head=new String[2];
        head[0]="archive";
        req.setAttribute("head",head);

        //creates Paging Model
        String url=req.getRequestURI()+"?";
        PagingModel pm=new PagingModel(postDAO.getAmountOfArchivePosts(),page,url);
        req.setAttribute("page_model",pm);
        //creates list of archive posts start at `SqlOffset`
        List<Post> archivePosts=postDAO.getArchivePosts(pm.getSqlOffset(),pm.getPAGE_ROWS());
        req.setAttribute("posts", archivePosts);

        //close connection
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("/view/post_list.jsp").forward(req, resp);
    }
}
