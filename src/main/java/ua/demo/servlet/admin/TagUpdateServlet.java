package ua.demo.servlet.admin;

import ua.demo.dao.TagDAO;
import ua.demo.dao.UserDAO;
import ua.demo.dao.impl.TagDAOImpl;
import ua.demo.dao.impl.UserDAOImpl;
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
 * Created by Sergey on 08.06.2015.
 */
public class TagUpdateServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User curUser=(User)req.getSession(false).getAttribute("curuser");
        req.setAttribute("curuser",curUser);

        //get query string parameters
        String idStr=req.getParameter("id");
        String tagName=req.getParameter("tag");


        //check paramameters
        boolean error=false;
        String errorMsg="";


        if ((tagName==null)||(tagName.isEmpty())) {
            error=true;
            errorMsg+="empty tag";
        } else {
            //defense of sql injections
            tagName=tagName.replace("'","`");
        }


        int id=-1;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception ex) {}

        if (error){
            String[] head=new String[2];
            head[0]="incorrect information:";
            head[1]=errorMsg;
            req.setAttribute("head",head);

            req.getRequestDispatcher("/view/message.jsp").forward(req, resp);
        }
        else {
            //create connection
            ConnectionFactory conf= ConnectionFactoryFactory.getConnectionFactory();
            Connection con=conf.getConnection();

            //create tag
            Tag tag=new Tag();
            tag.setId(id);
            tag.setTagName(tagName);


            //add tag
            TagDAO tagDao=new TagDAOImpl(con);
            boolean res=tagDao.addTag(tag);

            //close connection
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (res) {
                //ok tag was added

                String[] head=new String[2];
                head[0]="Ok:";
                head[1]="";
                req.setAttribute("head",head);

                req.getRequestDispatcher("/view/message.jsp").forward(req, resp);
            }else {
                //error
                String[] head=new String[2];
                head[0]="ERROR:";
                head[1]="";
                req.setAttribute("head",head);

                req.getRequestDispatcher("/view/message.jsp").forward(req, resp);
            }
        }
    }
}
