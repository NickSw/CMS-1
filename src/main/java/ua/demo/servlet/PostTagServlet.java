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

public class PostTagServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //check request string parameters
        String param = req.getParameter("id");
        int id = 0;
        if ((param == null) || (param.isEmpty())) {
            resp.sendRedirect("/");
        } else {
            try {
                id = Integer.parseInt(param);
            } catch (Exception ex) {
                resp.sendRedirect("/");
                return;
            }

            //creates connection
            ConnectionFactory conf = ConnectionFactoryFactory.getConnectionFactory();
            Connection con = conf.getConnection();

            //creates list of tags
            TagDAO tagDAO = new TagDAOImpl(con);
            List<Tag> tags = tagDAO.getAll();
            req.setAttribute("tags", tags);

            //creates head
            //show tag name
            Tag tag = tagDAO.getById(id);
            //check if tag with given id exists
            if (tag == null) {
                resp.sendRedirect("/");
                return;
            } else
            {
                String[] head=new String[2];
                head[0]=tag.getTagName();
                req.setAttribute("head", head);
            }

            //creates list of recent posts
            PostDAO postDAO = new PostDAOImpl(con);
            List<Post> recPosts = postDAO.getRecent();
            req.setAttribute("recent_posts", recPosts);

            //creates archive dates
            List<Date> dates=postDAO.getArchiveDates();
            req.setAttribute("archive_dates",dates);

            //pagination
            //check parameter value
            String pageParam=req.getParameter("page");
            int page;
            try {
                page = Integer.parseInt(pageParam);
            }catch (Exception ex) {page=1;}
            if (page<1) page=1;

            //creates Paging Model
            String url=req.getRequestURI()+"?id="+id+"&";
            PagingModel pm=new PagingModel(postDAO.getAmountOfTagPosts(id),page,url);
            req.setAttribute("page_model",pm);
            //creates list of posts with tag id start at `SqlOffset`
            List<Post> tagPosts=postDAO.getByTag(id, pm.getSqlOffset(), pm.getPAGE_ROWS());
            if (tagPosts == null) {
                resp.sendRedirect("/");
                return;
            } else req.setAttribute("posts", tagPosts);



            //close connection
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            req.getRequestDispatcher("/view/post_list.jsp").forward(req, resp);
        }
    }
}
