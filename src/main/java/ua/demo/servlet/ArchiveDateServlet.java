package ua.demo.servlet;

import ua.demo.dao.PostDAO;
import ua.demo.dao.TagDAO;
import ua.demo.dao.impl.PostDAOImpl;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*
* servlet show posts from archive that were created in certain month and year
*
* Created by Sergey on 20.05.2015.
*/
public class ArchiveDateServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //check request string parameters
        String dateStr=req.getParameter("date");
        Date date;
        try {
            SimpleDateFormat dateLong=new SimpleDateFormat("yyyy-MM");
            date=dateLong.parse(dateStr);

        } catch (ParseException e) {
            e.printStackTrace();
            resp.sendRedirect("/");
            return;
        }

            //check parameter value for pagination
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
            head[0]="Archive:";

            SimpleDateFormat dateShort=new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
            head[1]=dateShort.format(date);

            req.setAttribute("head",head);


            //creates Paging Model
            String url=req.getRequestURI()+"?date="+dateStr+"&";
            PagingModel pm=new PagingModel(postDAO.getAmountOfYearAndMonthPostsArchive(date),page,url);
            req.setAttribute("page_model",pm);

            //creates list of archive posts by year and month
            List<Post> posts=postDAO.getByYearAndMonthArchive(date,pm.getSqlOffset(),pm.getPAGE_ROWS());
            req.setAttribute("posts",posts);


            //close connection
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            req.getRequestDispatcher("/view/post_list.jsp").forward(req, resp);
        }
   }

