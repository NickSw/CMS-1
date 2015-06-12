package ua.demo.servlet.admin;

import ua.demo.dao.PostAdminDAO;
import ua.demo.dao.impl.PostAdminDAOImpl;
import ua.demo.entity.Post;
import ua.demo.entity.User;
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
import java.util.List;

/**
 * servlet get all posts from DB and render them as table.
 * servlet can obtain query string to list posts in different order
 *
 * Created by Sergey on 03.06.2015.
 */
public class PostsListServlet extends HttpServlet{
   //number of posts in list
    private final int ROW_NUM_ADMIN=10;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get current user from session
        User curUser=(User)req.getSession(false).getAttribute("curuser");
        req.setAttribute("curuser",curUser);

        //check parameter value `order`
        String orderStr=req.getParameter("order");
        int order=2;
        try {
            order = Integer.parseInt(orderStr);
        }catch (Exception ex) {order=2;}
        if (order<0) order=2;


        //check parameter value `page`
        String param=req.getParameter("page");
        int page;
        try {
            page = Integer.parseInt(param);
        }catch (Exception ex) {page=1;}
        if (page<1) page=1;

        //creates connection
        ConnectionFactory conf = ConnectionFactoryFactory.getConnectionFactory();
        Connection con = conf.getConnection();


        PostAdminDAO postDAO = new PostAdminDAOImpl(con);

        //creates Paging Model
        String url=req.getRequestURI()+"?order="+order+"&";
        PagingModel pm=new PagingModel(postDAO.getAmountOfAllPosts(),page,url,ROW_NUM_ADMIN);
        req.setAttribute("page_model",pm);
        //creates list of all posts start at `SqlOffset`
        List<Post> allPosts=postDAO.getAll(pm.getSqlOffset(),pm.getPAGE_ROWS(),order);
        req.setAttribute("posts", allPosts);

        req.setAttribute("order",order);

        //close connection
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("/view/posts_list_admin.jsp").forward(req, resp);
    }
}
