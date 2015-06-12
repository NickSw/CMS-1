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
import java.util.Date;
import java.util.List;


/**
 * servlet search some words in title or/and in content of post and if it find them show appropriate posts.
 * search parameters and words servlet receives via query string
 *
 * Created by Sergey on 13.05.2015.
 */
public class SearchServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String text=req.getParameter("text");
        String title=req.getParameter("title");
        String content=req.getParameter("content");
        boolean inTitle;
        boolean inContent;

        //check request string parameters
        if ((text==null)||(text.equals("")))
        {
            resp.sendRedirect("/");
            return;
        }
        else
        {
            if ((title==null)||(title.equals(""))) inTitle=false;
            else inTitle=true;
            if ((content==null)||(content.equals(""))) inContent=false;
            else inContent=true;
            if(!inTitle&&!inContent) inTitle=true;

            //defence of SQL injection
            //changes all symbols " and ' to `space`
            text=text.replace('"',' ');
            text=text.replace("'"," ");

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
            //creates search string for rendering in page
            String str="\""+text+"\""+" (in"+(inTitle?" title":"")+((inTitle&&inContent)?" and ":"")
                    +(inContent?" text)":")");
            String[] head=new String[2];
            head[0]="Search by:";
            head[1]=str;
            req.setAttribute("head",head);

            //creates Paging Model
            String url=req.getRequestURI()+"?text="+text+"&"+"title="+(inTitle?"true":"")+"&"+"content="+(inContent?"true":"")+"&";
            PagingModel pm=new PagingModel(postDAO.getAmountOfSearchedPosts(text, inTitle, inContent),page,url);
            req.setAttribute("page_model",pm);

            //creates list of searched posts start at `SqlOffset`
            List<Post> posts=postDAO.getSearched(text,inTitle,inContent,pm.getSqlOffset(),pm.getPAGE_ROWS());
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
}
